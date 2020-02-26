package DynamicContentDelivery.core;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class FileState extends TimerTask {
    public static Map<Long, UserContext> contextMap = new HashMap<Long, UserContext>();
    private static Queue<String> contents = new ArrayBlockingQueue<String>(10);
    private static RandomAccessFile randomAccessFile;
    private static int global_num_lines = 0;
    private String file_loc;

    public FileState(String file_loc) throws IOException {
        this.file_loc = file_loc;
    }

    public void init_Queue() throws IOException {
        randomAccessFile = new RandomAccessFile(this.file_loc, "r");
        Scanner scanner = new Scanner(new FileReader(randomAccessFile.getFD()));
        while (scanner.hasNextLine()) {
            if (contents.size() == 10) contents.remove();
            contents.add(scanner.nextLine());
            ++global_num_lines;
        }
    }


    public void insert(String new_line) {
        ++global_num_lines;
        if (!contents.isEmpty()) contents.remove();
        contents.add(new_line);
    }


    public Queue<String> getContents() {
        Queue<String> copy_contents = new ArrayDeque<String>(contents);
        return copy_contents;
    }

    public long getFilePointer() throws IOException {
        return randomAccessFile.getFilePointer();
    }

    public int getGlobal_num_lines() {
        return global_num_lines;
    }

    @Override
    public void run() {
        if (randomAccessFile == null) {
            System.out.println("Pointer is Null!");
            try {
                this.init_Queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            Scanner scanner = new Scanner(new FileReader(randomAccessFile.getFD()));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.length() == 0) continue;
                this.insert(line);
                for (UserContext userContext: contextMap.values()) {
                        userContext.append(line);
                }
                System.out.println("Updated Global State and local states... inserted  : " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
