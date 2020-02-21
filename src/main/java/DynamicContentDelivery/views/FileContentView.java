package DynamicContentDelivery.views;

import DynamicContentDelivery.core.FileContent;
import io.dropwizard.views.View;

import java.util.ArrayList;


public class FileContentView extends View {

    private final String fileName;
    private ArrayList<String> lines;

    public FileContentView(FileContent fileContent) {
        super("fileContent.ftl");
        this.lines = fileContent.getLines();
        this.fileName = fileContent.getFileName();
    }

    public ArrayList<String> getLines() {
        return this.lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }

    public String getFileName() {
        return this.fileName;
    }

}