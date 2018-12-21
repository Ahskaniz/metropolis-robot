package com.metropolis.robot.app.service;

import com.google.maps.model.LatLng;
import com.metropolis.robot.domain.PollutionLevel;
import com.metropolis.robot.domain.Report;

public class ReportingService {

    Report generateReport(LatLng position, PollutionLevel pollutionLevel, String name) {
        return new Report(
                position,
                pollutionLevel,
                name
        );
    }

}
