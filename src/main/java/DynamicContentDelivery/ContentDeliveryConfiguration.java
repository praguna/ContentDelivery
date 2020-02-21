package DynamicContentDelivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotEmpty;

public class ContentDeliveryConfiguration extends Configuration {
    @NotEmpty
    private String fileName = "test_file.txt";

    @JsonProperty
    public String getFileName() {
        return fileName;
    }

    @JsonProperty
    public void setFileName() {
        this.fileName = fileName;
    }

}
