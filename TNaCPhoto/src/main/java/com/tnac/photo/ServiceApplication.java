package com.tnac.photo;

import com.tnac.photo.config.ServiceConfiguration;
import com.tnac.photo.resource.ServiceResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServiceApplication extends Application<ServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new ServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(ServiceConfiguration configuration,
            Environment environment) {
        final ServiceResource resource = new ServiceResource(
                configuration.getTemplate(),
                configuration.getDefaultName());
        environment.jersey().register(resource);
    }

}
