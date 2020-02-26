package DynamicContentDelivery.core;

import java.util.ArrayList;
import java.util.TimerTask;

class UserContext extends TimerTask {
    private ArrayList<String> lines = new ArrayList<String>();
    private long file_pointer;
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

    @Override
    public void run() {
        System.out.println("Checking up user-connect..");
        if (!this.remove) {
            set_remove(true);
            System.out.println(String.format("User %d Persists !!",this.key));
        } else {
            FileState.contextMap.remove(this.key);
            System.out.println(String.format("Removed a user %d from map!!",this.key));
            this.cancel();
        }
    }
}
