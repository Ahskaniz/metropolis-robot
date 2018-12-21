package com.metropolis.robot.app.service

import com.google.maps.model.LatLng
import com.metropolis.robot.app.util.CoordinateUtils
import com.metropolis.robot.domain.MonitoringStation
import spock.lang.Specification

class MonitoringServiceSpec extends Specification {

    def "Empty monitoring statios returns empty list"() {
        given:
        MonitoringService monitoringService = new MonitoringService(
                new CoordinateUtils(),
                Arrays.asList(),
                0
        )

        when:
        def stations = monitoringService.retrieveActiveMonitoringStations(new LatLng(0, 0))

        then:
        stations == Arrays.asList()
    }

    def "Non visited monitoring station in rage is returned"() {
        given:
        MonitoringStation inRange = new MonitoringStation("Some station in range", new LatLng(51.5539700, -0.12843000)) //6 meters
        MonitoringStation outRange = new MonitoringStation("Some station out range", new LatLng(51.501299, -0.141935)) // 6000 meters
        MonitoringService monitoringService = new MonitoringService(
                new CoordinateUtils(),
                Arrays.asList(inRange, outRange),
                100
        )

        when:
        def stations = monitoringService.retrieveActiveMonitoringStations(new LatLng(51.55391000, -0.12844000))

        then:
        stations == Arrays.asList(inRange)
    }

    def "Several non visited monitoring station in rage are returned"() {
        given:
        MonitoringStation inRange = new MonitoringStation("Some station in range", new LatLng(51.5539700, -0.12843000)) //6 meters
        MonitoringStation inRange2 = new MonitoringStation("Some station in range", new LatLng(51.55387900, -0.12831000)) //9 meters
        MonitoringStation inRange3 = new MonitoringStation("Some station in range", new LatLng(51.55457500, -0.12830300)) //74 meters
        MonitoringStation outRange = new MonitoringStation("Some station out range", new LatLng(51.501299, -0.141935)) // 6000 meters
        MonitoringService monitoringService = new MonitoringService(
                new CoordinateUtils(),
                Arrays.asList(inRange, inRange2, inRange3, outRange),
                100
        )

        when:
        def stations = monitoringService.retrieveActiveMonitoringStations(new LatLng(51.55391000, -0.12844000))

        then:
        stations.containsAll(Arrays.asList(inRange, inRange2, inRange3))
    }

    def "Several visited monitoring station in rage are not returned"() {
        given:
        MonitoringStation inRange = new MonitoringStation("Some station in range", new LatLng(51.5539700, -0.12843000)) //6 meters
        MonitoringStation inRange2 = new MonitoringStation("Some station in range", new LatLng(51.55387900, -0.12831000)) //9 meters
        MonitoringStation outRange = new MonitoringStation("Some station out range", new LatLng(51.501299, -0.141935)) // 6000 meters
        MonitoringService monitoringService = new MonitoringService(
                new CoordinateUtils(),
                Arrays.asList(inRange, inRange2, outRange),
                100
        )

        when:
        def firstRun = monitoringService.retrieveActiveMonitoringStations(new LatLng(51.55391000, -0.12844000))
        def secondRund = monitoringService.retrieveActiveMonitoringStations(new LatLng(51.55391000, -0.12844000))

        then:
        firstRun.containsAll(Arrays.asList(inRange, inRange2))
        secondRund == Arrays.asList()
    }

}
