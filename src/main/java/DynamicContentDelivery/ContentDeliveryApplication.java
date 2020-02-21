package DynamicContentDelivery;

import DynamicContentDelivery.core.FileState;
import DynamicContentDelivery.resources.FileContentResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.io.IOException;
import java.util.Timer;

public class ContentDeliveryApplication extends Application<ContentDeliveryConfiguration> {
    public static void main(final String[] args) throws Exception {
        new ContentDeliveryApplication().run(args);
    }

    @Override
    public String getName() {
        return "content-delivery";
    }

    @Override
    public void initialize(final Bootstrap<ContentDeliveryConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(final ContentDeliveryConfiguration configuration,
                    final Environment environment) throws IOException {
        environment.jersey().register(new FileContentResource(configuration.getFileName()));
        Timer timer = new Timer();
        FileState fileState = new FileState(System.getProperty("user.dir") + "/" + configuration.getFileName());
        timer.schedule(fileState, 5000, 5000);
    }

}
