package com.metropolis.robot;

import com.metropolis.robot.app.exception.IllegalArgumentExceptionMapper;
import com.metropolis.robot.app.exception.NotFoundExceptionMapper;
import com.metropolis.robot.infra.config.RestAPIConfiguration;
import com.metropolis.robot.infra.rest.RobotEndpoint;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class RestAPI extends Application<RestAPIConfiguration> {

    @Override
    public String getName() {
        return "RestAPI";
    }

    @Override
    public void initialize(Bootstrap<RestAPIConfiguration> bootstrap) { }

    @Override
    public void run(RestAPIConfiguration configuration,
                    Environment environment) {
        RobotEndpoint robotEndpoints = new RobotEndpoint();
        environment.jersey().register(robotEndpoints);
        environment.jersey().register(new NotFoundExceptionMapper());
        environment.jersey().register(new IllegalArgumentExceptionMapper());
    }

}
