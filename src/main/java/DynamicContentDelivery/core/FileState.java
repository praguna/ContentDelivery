package DynamicContentDelivery.core;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FileState extends TimerTask {
    static Map<Long, UserContext> contextMap = new HashMap<>();
    private static Queue<String> contents = new ArrayBlockingQueue<>(10);
    private static RandomAccessFile randomAccessFile;
    private static int global_num_lines = 0;
    private static String file_loc;
    private static WatchService watchService;
    private static  ReadWriteLock readWriteLock;

    public FileState(String file_loc) throws IOException {
        FileState.setFileName(file_loc);
        Path path = Paths.get(System.getProperty("user.dir"));
        readWriteLock = new ReentrantReadWriteLock();
        watchService = path.getFileSystem().newWatchService();
        path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);
        System.out.println("Initializing the Queue ....");
        try {
                init_Queue(file_loc);
            }
        catch (IOException e) {
                e.printStackTrace();
        }
    }

    private static void init_Queue(String file_loc) throws IOException {
        randomAccessFile = new RandomAccessFile(file_loc, "r");
        Scanner scanner = new Scanner(new FileReader(randomAccessFile.getFD()));
        while (scanner.hasNextLine()) {
            if (contents.size() == 10) contents.remove();
            contents.add(scanner.nextLine());
            ++global_num_lines;
        }
    }

    private static void update_file() throws InterruptedException {
        System.out.println("Monitoring File for changes .....");
        WatchKey wk = watchService.take();
        for(WatchEvent<?> event : wk.pollEvents()){
            final Path changed = (Path) event.context();
            if (changed.endsWith(FileState.file_loc)) {
                System.out.println("File has changed !!");
                try {
                    readWriteLock.writeLock().lock();
                    write_context();
                    readWriteLock.writeLock().unlock();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        wk.reset();
    }

    private static void write_context() throws IOException {
        Scanner scanner = new Scanner(new FileReader(randomAccessFile.getFD()));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.length() == 0) continue;
                insert(line);
                for (UserContext userContext: contextMap.values()) {
                        userContext.append(line);
                }
                System.out.println("Updated Global State and local states... inserted  : " + line);
            }
    }

    private static void setFileName(String fileName) {
        FileState.file_loc = fileName;
    }


    private static void insert(String new_line) {
        ++global_num_lines;
        if (!contents.isEmpty()) contents.remove();
        contents.add(new_line);
    }


    public static Queue<String> getContents() {
        return new ArrayDeque<>(contents);
    }

    public long getFilePointer() throws IOException {
        return randomAccessFile.getFilePointer();
    }

    public static int getGlobal_num_lines() {
        return global_num_lines;
    }

    @Override
    public void run() {
        if (randomAccessFile == null) {
            System.out.println("Initializing the Queue ....");
            try {
                init_Queue(file_loc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            update_file();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
