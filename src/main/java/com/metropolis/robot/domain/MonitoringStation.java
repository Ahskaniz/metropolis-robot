package com.metropolis.robot.domain;

import com.google.maps.model.LatLng;

public class MonitoringStation {

    private final String name;
    private LatLng position;

    public MonitoringStation(String name, LatLng position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public LatLng getPosition() {
        return position;
    }
}
