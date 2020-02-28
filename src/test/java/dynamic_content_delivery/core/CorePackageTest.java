package dynamic_content_delivery.core;

import dynamic_content_delivery.views.FileContentView;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class CorePackageTest {
    private final long key = 1233434;
    private static final String file_loc = "test_file.txt";
    private static final ArrayList<String> list= new ArrayList<>(Arrays.asList(
            "line 1","line 2","line 3"
    ));
    private static final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(3);
    @Test
    public void testUserContext(){
        UserContext userContext = new UserContext(key);
        userContext.set_remove(false);
        assert(!userContext.getRemove());
        assert (userContext.getKey() == key);
        userContext.append("line 1");
        assert(userContext.getLines().size() == 1  && userContext.getLines().get(0).equals("line 1"));
        userContext.clear();
        assert (userContext.getLines().isEmpty());
        FileState.contextMap.put(key,userContext);
        userContext.set_remove(true);
        userContext.run();
        assert (!FileState.contextMap.containsKey(key));
        userContext.set_remove(false);
        FileState.contextMap.put(key,userContext);
        userContext.run();
        assert (userContext.getRemove());
    }

    @Test
    public void testFileContentAndView() throws IOException {
        queue.add("line 1");
        queue.add("line 2");
        queue.add("line 3");
        FileContent fileContent_default = new FileContent(file_loc,key,queue,key);
        assert(queue.isEmpty());
        UserContext userContext = new UserContext(key);
        FileState.contextMap.put(key,userContext);
        new FileContent(file_loc, key, key);
        assert(!userContext.getRemove());
        assert (userContext.getLines().isEmpty());
        assert (fileContent_default.getLines().size() == 3);
        assert (fileContent_default.getFileName() == file_loc);
        assert (fileContent_default.getId() == key);
        FileContentView fileContentView = new FileContentView(fileContent_default);
        assert (!fileContentView.getLines().isEmpty());
        assert (fileContentView.getId() == key);
        assert (!fileContentView.getFileName().isEmpty());
        fileContentView.setLines(list);
        assert(fileContentView.getLines() == list);
    }

    @Test
    public void testFileState() throws IOException, InterruptedException {
        FileState fileState = new FileState(file_loc);
        assert (FileState.getGlobal_num_lines() > 0);
        assert(FileState.getContents().size() == 10);
        assert (fileState.getFilePointer()>0);
        System.out.println("------------------Test Environment Add few lines to the file-----------------");
        fileState.run();
        FileState.write_context();
    }

    public static void addDummyUser(long key){
        FileState.contextMap.put(key,new UserContext(key));
    }

}
