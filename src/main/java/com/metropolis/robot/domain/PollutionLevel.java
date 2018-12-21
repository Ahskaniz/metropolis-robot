package com.metropolis.robot.domain;

public enum PollutionLevel {

    GOOD("Good"),
    MODERATE("Moderate"),
    USG("USG"),
    UNHEALTHY("Unhealthy");

    private String name;

    PollutionLevel(String name) {
        this.name = name;
    }
}
