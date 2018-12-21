package com.metropolis.robot.domain;

import com.google.maps.model.LatLng;

public class Robot {

    private final String name;
    private int velocity;
    private boolean inRoute;
    private LatLng currentPosition;
    private boolean activated;
    private double metersMovedSinceLastLecture;
    private boolean disconnect;

    public Robot(int velocity) {
        this.velocity = velocity;
        this.name = "robot";
        this.activated = false;
        this.metersMovedSinceLastLecture = 0;
        this.inRoute = false;
        this.disconnect = false;
    }

    public String getName() {
        return name;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public LatLng getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(LatLng currentPosition) {
        this.currentPosition = currentPosition;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public double getMetersMovedSinceLastLecture() {
        return metersMovedSinceLastLecture;
    }

    public void setMetersMovedSinceLastLecture(double metersMovedSinceLastLecture) {
        this.metersMovedSinceLastLecture = metersMovedSinceLastLecture;
    }

    public boolean isInRoute() {
        return inRoute;
    }

    public void setInRoute(boolean inRoute) {
        this.inRoute = inRoute;
    }

    public boolean isDisconnected() {
        return disconnect;
    }

    public void disconnect() {
        this.disconnect = true;
    }
}
