package DynamicContentDelivery.core;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Timer;

public class FileContent {
    ArrayList<String> lines = new ArrayList<String>();
    String name;
    long hash_code;

    public FileContent(String name, long hash_code, Queue<String> lines, long fp) {
        this.hash_code = hash_code;
        while (!lines.isEmpty()) {
            this.lines.add(lines.remove());
        }
        this.name = name;
        Timer timer = new Timer();
        UserContext userContext = new UserContext(this.lines, fp, this.hash_code);
        timer.schedule(userContext, 7000, 7000);
        FileState.contextMap.put(this.hash_code, userContext);
    }

    public FileContent(String name, long hash_code, String path) throws IOException {
        System.out.println("Found in map!");
        this.name = name;
        this.hash_code = hash_code;
        this.read_file(path);
        this.lines = FileState.contextMap.get(this.hash_code).getLines();
    }

    public ArrayList<String> getLines() {
        return this.lines;
    }

    void read_file(String path) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(System.getProperty("user.dir") + "/" + name, "r");
        raf.seek(FileState.contextMap.get(this.hash_code).getFile_pointer());
        Scanner sc = new Scanner(new FileReader(raf.getFD()));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.length() == 0) continue;
            FileState.contextMap.get(this.hash_code).append(line);
        }
        FileState.contextMap.get(this.hash_code).set_remove(false);
        FileState.contextMap.get(this.hash_code).setFile_pointer(raf.getFilePointer());
    }

    public String getFileName() {
        return name;
    }
}
