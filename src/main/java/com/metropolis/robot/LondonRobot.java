package com.metropolis.robot;

import com.google.maps.model.LatLng;
import com.metropolis.robot.app.service.MonitoringService;
import com.metropolis.robot.app.service.PollutionService;
import com.metropolis.robot.app.service.ReportingService;
import com.metropolis.robot.app.service.RobotService;
import com.metropolis.robot.app.util.CoordinateUtils;
import com.metropolis.robot.app.util.RandomUtils;
import com.metropolis.robot.app.util.ThreadUtils;
import com.metropolis.robot.domain.MonitoringStation;

import java.util.Arrays;
import java.util.List;

public class LondonRobot implements Runnable {

    @Override
    public void run() {
        List<MonitoringStation> monitoringStations = Arrays.asList(
                new MonitoringStation("Buckingham Palace", new LatLng(51.501299, -0.141935)),
                new MonitoringStation("Temple Station", new LatLng(51.510852, -0.114165))
        );
        RobotService.getInstance()
                .monitoringService(
                        new MonitoringService(
                                new CoordinateUtils(),
                                monitoringStations,
                                100
                        )
                )
                .pollutionService(new PollutionService(new RandomUtils()))
                .reportingService(new ReportingService())
                .coordinateUtils(new CoordinateUtils())
                .threadUtils(new ThreadUtils())
                .run();
    }
}
