package dynamic_content_delivery.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class DeltaContent {
    private long id;
    private ArrayList<String> content;
    public DeltaContent(long id,ArrayList<String> content){
        this.id = id;
        this.content = content;
    }
    @JsonProperty
    public long getId(){
        return this.id;
    }
    @JsonProperty
    public ArrayList<String> getContent(){
        return this.content;
    }
}

