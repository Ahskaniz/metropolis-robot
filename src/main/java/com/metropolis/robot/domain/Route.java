package com.metropolis.robot.domain;

import com.google.maps.model.LatLng;

import java.util.List;

public class Route {

    private final List<LatLng> points;

    public Route(List<LatLng> points) {
        this.points = points;
    }

    public List<LatLng> getPoints() {
        return points;
    }

}
