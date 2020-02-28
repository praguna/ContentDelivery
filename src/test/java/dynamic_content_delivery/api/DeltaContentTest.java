package dynamic_content_delivery.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import static org.assertj.core.api.Assertions.assertThat;
import static io.dropwizard.testing.FixtureHelpers.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class DeltaContentTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    @Test
    public void serializesToJSON() throws Exception {
        long id = 12367782;
        ArrayList<String> content = new ArrayList<String>(Arrays.asList(
                "line 1","line 2","line 3"
        ));
        final DeltaContent new_content = new DeltaContent(id,content);
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/delta_content.json"), DeltaContent.class));
        assertThat(MAPPER.writeValueAsString(new_content)).isEqualTo(expected);
    }
}
