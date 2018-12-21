package com.metropolis.robot.infra.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RunDto {

    private String route;
    private int velocity;

    @JsonCreator
    public RunDto(@JsonProperty("route") String route, @JsonProperty("velocity") int velocity) {
        this.route = route;
        this.velocity = velocity;
    }

    @JsonProperty
    public String getRoute() {
        return route;
    }

    @JsonProperty
    public void setRoute(String route) {
        this.route = route;
    }

    @JsonProperty
    public int getVelocity() {
        return velocity;
    }

    @JsonProperty
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
