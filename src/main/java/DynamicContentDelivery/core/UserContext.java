package DynamicContentDelivery.core;

import java.util.ArrayList;
import java.util.TimerTask;

class UserContext extends TimerTask {
    private ArrayList<String> lines = new ArrayList<String>();
    private long file_pointer;
    private boolean remove;
    private long key;

    UserContext(ArrayList<String> lines, long fp, long key) {
        this.lines = lines;
        this.file_pointer = fp;
        this.remove = true;
        this.key = key;
    }

    ArrayList<String> getLines() {
        return this.lines;
    }

    long getFile_pointer() {
        return this.file_pointer;
    }

    void setFile_pointer(long fp) {
        this.file_pointer = fp;
    }

    void append(String line) {
        lines.add(line);
    }

    void set_remove(boolean value) {
        this.remove = value;
    }

    @Override
    public void run() {
        System.out.println("Checking up user-connect..");
        if (!this.remove) {
            set_remove(true);
            System.out.println("User Persists !!");
        } else {
            FileState.contextMap.remove(this.key);
            System.out.println("Removed a user from map!!");
            this.cancel();
        }
    }
}
