package com.metropolis.robot.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.maps.model.LatLng;

public class Report {

    private long timestamp;
    private LatLng location;
    private PollutionLevel level;
    private String source;

    @JsonCreator
    public Report(@JsonProperty("location") LatLng location, @JsonProperty("level") PollutionLevel level, @JsonProperty("source") String source) {
        this.timestamp = System.currentTimeMillis();
        this.location = location;
        this.level = level;
        this.source = source;
    }

    @JsonProperty
    public long getTimestamp() {
        return timestamp;
    }

    @JsonProperty
    public LatLng getLocation() {
        return location;
    }

    @JsonProperty
    public PollutionLevel getLevel() {
        return level;
    }

    @JsonProperty
    public String getSource() {
        return source;
    }

}
