package dynamic_content_delivery.core;

import java.util.ArrayList;
import java.util.TimerTask;

class UserContext extends TimerTask {
    private ArrayList<String> lines = new ArrayList<String>();
    private boolean remove;
    private long key;

    UserContext(long key) {
        this.remove = true;
        this.key = key;
    }

    ArrayList<String> getLines() {
        return this.lines;
    }

    void append(String line) {
        lines.add(line);
    }

    void set_remove(boolean value) {
        this.remove = value;
    }

    void clear(){
        this.lines.clear();
    }

    boolean getRemove(){
        return this.remove;
    }

    @Override
    public void run() {
        System.out.println("Checking up user-connect..");
        if (!this.remove) {
            set_remove(true);
            System.out.println(String.format("User id :%d Persists !!", this.key));
        } else {
            FileState.contextMap.remove(this.key);
            System.out.println(String.format("Removed a user id:  %d from map!!",this.key));
            this.cancel();
        }
    }

    public long getKey() {
        return this.key;
    }
}
