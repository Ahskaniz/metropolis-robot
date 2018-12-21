package com.metropolis.robot.app.usecase;

import com.google.maps.internal.ratelimiter.Preconditions;
import com.google.maps.model.LatLng;
import com.metropolis.robot.app.service.RobotService;
import com.metropolis.robot.app.util.PolylineUtils;
import com.metropolis.robot.domain.Report;
import com.metropolis.robot.domain.Robot;
import com.metropolis.robot.domain.Route;

import javax.ws.rs.NotFoundException;
import java.util.Arrays;
import java.util.List;

public class RobotUseCase {

    private final RobotService robotService;

    public RobotUseCase() {
        this.robotService = RobotService.getInstance();
    }

    public void run(String polyline, int velocity) {
        Preconditions.checkArgument(Arrays.asList(1, 2, 3).contains(velocity), "Velocity should be between 1 and 3");

        List<LatLng> points = PolylineUtils.getPolyline(polyline);
        if (points.size() <= 1) throw new IllegalArgumentException("More points needed to move a robot");
        if (robotService.isRobotServiceDisconnected()) throw new NotFoundException("Service has been disconnected");
        robotService
                .stopCurrentRoute()
                .setRobot(new Robot(velocity))
                .setRoute(new Route(points));
    }

    public void doAction(String action) {
        switch (action.toLowerCase()) {
            case "start":
                robotService.startRobot();
                break;
            case "stop":
                robotService.stopRobot();
                break;
            default:
                throw new IllegalArgumentException("Action should be either start or stop");
        }
    }

    public Report report() {
        return robotService.generateReport("rest_api");
    }

    public void disconnect() {
        if (robotService.isRobotServiceDisconnected()) throw new NotFoundException("Service has been disconnected");
        robotService.disconnectRobotService();
    }

}
