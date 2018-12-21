package com.metropolis.robot.app.service;

import com.google.maps.model.LatLng;
import com.metropolis.robot.app.util.CoordinateUtils;
import com.metropolis.robot.domain.MonitoringStation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoringService {

    private Map<MonitoringStation, Boolean> monitoringStations = new HashMap<>();
    private int monitoringRadiusInMeters;
    private CoordinateUtils coordinateUtils;

    public MonitoringService(CoordinateUtils coordinateUtils, List<MonitoringStation> monitoringStations, int monitoringRadiusInMeters) {
        this.coordinateUtils = coordinateUtils;
        this.monitoringRadiusInMeters = monitoringRadiusInMeters;
        for (MonitoringStation monitoringStation : monitoringStations) {
            this.monitoringStations.put(monitoringStation, false);
        }
    }

    List<MonitoringStation> retrieveActiveMonitoringStations(LatLng position) {
        List<MonitoringStation> reportingStations = new ArrayList<>();
        monitoringStations.forEach((station, visited) -> {
            double distanceInMeters = coordinateUtils.distanceInMeters(position, station.getPosition());
            if (!visited && distanceInMeters <= monitoringRadiusInMeters) {
                reportingStations.add(station);
                monitoringStations.put(station, true);
            }
        });

        return reportingStations;
    }

}
