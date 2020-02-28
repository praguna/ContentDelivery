package dynamic_content_delivery;

import org.junit.Test;

public class ConfigurationTest {
    @Test
    public void testConfiguration(){
        ContentDeliveryConfiguration contentDeliveryConfiguration = new ContentDeliveryConfiguration();
        assert (contentDeliveryConfiguration.getFileName() == "test_file.txt");
    }
}
