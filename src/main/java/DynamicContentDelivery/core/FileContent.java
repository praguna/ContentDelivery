package DynamicContentDelivery.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Timer;

public class FileContent {
    private ArrayList<String> lines = new ArrayList<String>();
    private String name;
    private long hash_code;
    private long id;

    public FileContent(String name, long hash_code, Queue<String> lines, long id) {
        this.hash_code = hash_code;
        this.id = id;
        while (!lines.isEmpty()) {
            this.lines.add(lines.remove());
        }
        this.name = name;
        Timer timer = new Timer();
        UserContext userContext = new UserContext(this.hash_code);
        timer.schedule(userContext, 7000, 7000);
        FileState.contextMap.put(this.hash_code, userContext);
    }

    public FileContent(String name, long hash_code, long id) throws IOException {
        this.id = id;
        this.name = name;
        this.hash_code = hash_code;
        UserContext userContext = FileState.contextMap.get(this.hash_code);
        this.lines = new ArrayList<String>(userContext.getLines());
        System.out.println("Found in map!");
        userContext.set_remove(false);
        userContext.clear();

    }
    public ArrayList<String> getLines() {
        return this.lines;
    }

    public String getFileName() {
        return name;
    }

    public long getId() {
        return this.id;
    }
}
