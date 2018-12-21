package com.metropolis.robot.app.service;

import com.google.common.annotations.VisibleForTesting;
import com.google.maps.model.LatLng;
import com.metropolis.robot.app.util.CoordinateUtils;
import com.metropolis.robot.app.util.ThreadUtils;
import com.metropolis.robot.domain.*;

import javax.ws.rs.NotFoundException;
import java.util.List;

public class RobotService implements Runnable {

    private static RobotService instance = null;;
    private boolean robotServiceDisconnected = false;

    private Route route;
    private Robot robot;
    private PollutionService pollutionService;
    private MonitoringService monitoringService;
    private ReportingService reportingService;
    private CoordinateUtils coordinateUtils;
    private ThreadUtils threadUtils;

    private final int pollutionLectureEveryMeters = 100;
    private final int automaticReportEverySeconds = 900;

    private int timeSinceLastReportInSeconds = 0;
    private boolean rerouteRequested = false;

    @VisibleForTesting
    private RobotService() {
    }

    public static RobotService getInstance() {
        if (instance == null) {
            instance = new RobotService();
        }

        return instance;
    }

    public RobotService setRobot(Robot robot) {
        if (this.robot == null) {
            this.robot = robot;
        }
        else {
            this.robot.setVelocity(robot.getVelocity());
        }
        System.out.println("Robot setup");
        return this;
    }

    public Robot getRobot() {
        return robot;
    }

    public void startRobot() {
        if (robot == null) throw new NotFoundException("There is no robot here");
        if (!robot.isActivated()) {
            robot.setActivated(true);
            robot.setInRoute(true);
        }
    }

    public void stopRobot() {
        if (robot == null) throw new NotFoundException("There is no robot here");
        if (robot.isActivated()) {
            robot.setActivated(false);
        }
    }

    public void disconnectRobotService() {
        if (robot == null) throw new NotFoundException("There is no robot here");
        robot.disconnect();
        robotServiceDisconnected = true;
        System.out.println("Robot service disconnected!");
    }

    public RobotService setRoute(Route route) {
        System.out.println("New route");
        this.route = route;
        rerouteRequested = true;
        return this;
    }

    public Route getRoute() {
        return route;
    }

    public boolean isRerouteRequested() {
        return rerouteRequested;
    }

    public boolean isRobotServiceDisconnected() {
        return robotServiceDisconnected;
    }

    public RobotService stopCurrentRoute() {
        if (robot != null && robot.isInRoute()) {
            System.out.println("In route");
            rerouteRequested = true;
            while (robot.isInRoute()) {
                threadUtils.sleep(1000);
                System.out.println("Waiting for robot to stop current route... ");
            }
        }
        System.out.println("Robot free!");
        return this;
    }

    public RobotService monitoringService(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
        return this;
    }

    public RobotService pollutionService(PollutionService pollutionService) {
        this.pollutionService = pollutionService;
        return this;
    }

    public RobotService reportingService(ReportingService reportingService) {
        this.reportingService = reportingService;
        return this;
    }

    public RobotService coordinateUtils(CoordinateUtils coordinateUtils) {
        this.coordinateUtils = coordinateUtils;
        return this;
    }

    public RobotService threadUtils(ThreadUtils threadUtils) {
        this.threadUtils = threadUtils;
        return this;
    }

    @Override
    public void run() {
        while (!robotServiceDisconnected) {
            do {
                threadUtils.sleep(5000);
                System.out.println("Waiting for route");
            } while (!rerouteRequested && !robotServiceDisconnected);
            if (serviceShouldStop()) break;

            startRobot();
            rerouteRequested = false;
            System.out.println("Starting route");
            LatLng currentPosition = robot.getCurrentPosition();
            LatLng currentPoint = (currentPosition != null) ?
                    currentPosition :
                    route.getPoints().get(0);
            System.out.println("Robot current position: " + currentPosition);
            System.out.println("Starting point: " + currentPoint);

            for (LatLng nextPoint : route.getPoints()) {
                System.out.println("Iterating over polyline points");
                currentPoint = moveUntilNextPoint(currentPoint, nextPoint);

                while (!robot.isActivated() && !rerouteRequested && !robot.isDisconnected()) {
                    System.out.println("Waiting stopped...");
                    int timeToSleepInSeconds = 5;
                    threadUtils.sleep(timeToSleepInSeconds * 1000);
                    timeSinceLastReportInSeconds += timeToSleepInSeconds;
                    checkReporting();
                }
                if (rerouteRequested) {
                    System.out.println("Reroute requested");
                    break;
                }
                if (serviceShouldStop()) break;

            }
            System.out.println("BEEP BOOP! Service finished!");
            robot.setInRoute(false);
        }

        LatLng currentPosition = null;
        if (robot != null) {
            currentPosition = robot.getCurrentPosition();
        }

        System.out.println("Robot deactivated at " + currentPosition + ". BEEP BOOP! Please come for me!");
    }

    private boolean serviceShouldStop() {
        if (robot.isDisconnected()) {
            robotServiceDisconnected = true;
            return true;
        }
        return false;
    }

    private LatLng moveUntilNextPoint(LatLng currentPoint, LatLng nextPoint) {
        while (currentPoint != nextPoint) {
            currentPoint = moveForwardTo(currentPoint, nextPoint);
            robot.setCurrentPosition(currentPoint);

            if (robot.getMetersMovedSinceLastLecture() >= pollutionLectureEveryMeters) {
                pollutionService.trackPollution();
                robot.setMetersMovedSinceLastLecture(0);
            }
            checkReporting();

            threadUtils.sleep(10);

            checkMonitoringPoints(currentPoint);

            if (!robot.isActivated()) {
                break;
            }
        }

        return currentPoint;
    }

    private LatLng moveForwardTo(LatLng currentPoint, LatLng endPoint) {
        timeSinceLastReportInSeconds += 1;
        double distance = coordinateUtils.distanceInMeters(currentPoint, endPoint);
        int velocity = robot.getVelocity();
        if (distance < velocity) {
            robot.setMetersMovedSinceLastLecture(robot.getMetersMovedSinceLastLecture() + distance);
            return endPoint;
        } else {
            robot.setMetersMovedSinceLastLecture(robot.getMetersMovedSinceLastLecture() + velocity);
            return coordinateUtils.nextPoint(currentPoint, endPoint, velocity);
        }
    }

    // Monitoring stations
    private void checkMonitoringPoints(LatLng position) {
        List<MonitoringStation> monitoringStations = monitoringService.retrieveActiveMonitoringStations(position);
        monitoringStations.forEach(monitoringStation -> System.out.println(generateReport(monitoringStation.getName())));
    }

    // Report
    private void checkReporting() {
        if (timeSinceLastReportInSeconds >= automaticReportEverySeconds) {
            System.out.println(generateReport(robot.getName()));
            timeSinceLastReportInSeconds = 0;
        }
    }

    public Report generateReport(String name) {
        LatLng currentPosition = null;
        if (robot != null) currentPosition = robot.getCurrentPosition();
        return reportingService.generateReport(
                currentPosition,
                pollutionService.getPollutionDescription(),
                name
        );
    }

}