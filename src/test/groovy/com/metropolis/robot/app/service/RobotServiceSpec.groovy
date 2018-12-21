package com.metropolis.robot.app.service

import com.google.maps.model.LatLng
import com.metropolis.robot.app.util.CoordinateUtils
import com.metropolis.robot.app.util.ThreadUtils
import com.metropolis.robot.domain.MonitoringStation
import com.metropolis.robot.domain.PollutionLevel
import com.metropolis.robot.domain.Robot
import com.metropolis.robot.domain.Route
import spock.lang.Specification

import javax.ws.rs.NotFoundException

class RobotServiceSpec extends Specification {

    def robotService
    def pollutionService = Mock(PollutionService)
    def threadUtils = Mock(ThreadUtils)

    void setup() {
        robotService = new RobotService()
        List<MonitoringStation> monitoringStations = Arrays.asList(
                new MonitoringStation("Buckingham Palace", new LatLng(51.501299, -0.141935)),
                new MonitoringStation("Temple Station", new LatLng(51.510852, -0.114165))
        )

        def coordinateUtils = new CoordinateUtils()
        robotService
                .reportingService(new ReportingService())
                .pollutionService(pollutionService)
                .monitoringService(new MonitoringService(
                    coordinateUtils,
                    monitoringStations,
                    100
                ))
                .coordinateUtils(coordinateUtils)
                .threadUtils(threadUtils)
    }

    def "Changing velocity of non existing robot adds robot"() {
        given:

        when:
        def initialRobot = robotService.getRobot()
        robotService.setRobot(new Robot(2))
        def currentRobot = robotService.getRobot()

        then:
        initialRobot == null
        currentRobot.velocity == 2
    }

    def "Changing velocity of existent robot only modifies robot velocity"() {
        given:
        robotService.setRobot(new Robot(3))
        robotService.getRobot().setCurrentPosition(new LatLng(0, 10))

        when:
        robotService.setRobot(new Robot(1))
        def currentRobot = robotService.getRobot()

        then:
        currentRobot.velocity == 1
        currentRobot.getCurrentPosition() == new LatLng(0, 10);
    }

    def "Starting a non existing robot throws exception"() {
        when:
        robotService.startRobot()

        then:
        thrown(NotFoundException)
    }

    def "Stopping a non existing robot throws exception"() {
        when:
        robotService.stopRobot()

        then:
        thrown(NotFoundException)
    }

    def "Starting a existing robot starts it"() {
        given:
        robotService.setRobot(new Robot(2))

        when:
        robotService.startRobot()

        then:
        robotService.getRobot().activated
        robotService.getRobot().inRoute
    }

    def "Stopping a existing robot stops it"() {
        given:
        robotService.setRobot(new Robot(2))

        when:
        robotService.stopRobot()

        then:
        !robotService.getRobot().activated
    }

    def "If there is no route routing is disabled for the robot"() {
        expect:
        !robotService.isRerouteRequested()
    }

    def "Adding a route enables routing for the robot"() {
        when:
        robotService.setRoute(new Route())

        then:
        robotService.isRerouteRequested()
    }

    def "Adding two route enables routing and modifies route for the robot"() {
        given:
        robotService.setRoute(new Route())
        Route secondRoute = new Route([new LatLng(0, 0)])

        when:
        robotService.setRoute(secondRoute)

        then:
        robotService.isRerouteRequested()
        robotService.getRoute() == secondRoute
    }

    def "Stopping current robot route without robot being in route does not request rerouting"() {
        given:
        robotService.setRobot(new Robot(2))

        when:
        robotService.stopCurrentRoute()

        then:
        !robotService.isRerouteRequested()
    }

    def "Stopping current robot route with robot being in route does request rerouting"() {
        given:
        def robot = Mock(Robot) {
            1 * isInRoute() >> true
            1 * isInRoute() >> true
            1 * isInRoute() >> true
            1 * isInRoute() >> false
        }
        robotService.setRobot(robot)

        when:
        robotService.stopCurrentRoute()

        then:
        2 * threadUtils.sleep(1000)
        robotService.isRerouteRequested()
    }

    def "Asking for report without existing running robot"() {
        given:
        def name = "some_name"

        when:
        def report = robotService.generateReport(name)

        then:
        report.level == null
        report.location == null
        report.source == name
    }

    def "Asking for report with existing running robot"() {
        given:
        LatLng initialPosition = new LatLng(51.504153, -0.216929)
        LatLng lastPosition = new LatLng(51.50415300, -0.22692900)

        // _ should be around ~347 (exact 2m/s) or at much 360 assuming loss of precision
        // for getMetersMovedSinceLastLecture is doubled, as it´s called twice per iteration
        // for isActivated >> true is the number + 2 (out interactions)
        def robot = Mock(Robot) {
            1 * isActivated() >> false
            _ * getVelocity() >> 2
            _ * getMetersMovedSinceLastLecture() >> 100
            _ * setMetersMovedSinceLastLecture(0)
            _ * isActivated() >> true
            _ * setCurrentPosition(_ as LatLng)
            1 * setInRoute(false)
            2 * isDisconnected() >> false
            1 * isDisconnected() >> true
            3 * getCurrentPosition() >> null >> lastPosition >> lastPosition
        }
        robotService
                .setRobot(robot)
                .setRoute(new Route([initialPosition, lastPosition]))
                .run()
        def name = "some_name"

        when:
        def report = robotService.generateReport(name)

        then:
        1 * pollutionService.getPollutionDescription() >> PollutionLevel.MODERATE
        report.level == PollutionLevel.MODERATE
        report.location == lastPosition
        report.source == name
    }

    def "Disconnecting non existing robot throws exception"() {
        when:
        robotService.disconnectRobotService()

        then:
        thrown(NotFoundException)
    }

    def "Disconnecting robot sets the robot as disconnected"() {
        given:
        robotService.setRobot(new Robot(2))

        when:
        robotService.disconnectRobotService()

        then:
        robotService.getRobot().isDisconnected()
    }

}
