package com.metropolis.robot.app.service

import com.metropolis.robot.app.util.RandomUtils
import com.metropolis.robot.domain.PollutionLevel
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class PollutionServiceSpec extends Specification {

    def randomUtils = Mock(RandomUtils)
    @Shared PollutionService pollutionService

    void setup() {
        pollutionService = new PollutionService(randomUtils);
    }

    def "Never tracked pollution returns empty"() {
        given:

        expect:
        pollutionService.getPollutionDescription() == null
    }

    @Unroll
    def "Tracked average pollution of #pollution returns average #expectedLevel"() {
        given:

        when:
        pollutionService.trackPollution()
        pollutionService.trackPollution()
        pollutionService.trackPollution()
        pollutionService.trackPollution()
        pollutionService.trackPollution()
        pollutionService.trackPollution()
        pollutionService.trackPollution()

        then:
        7 * randomUtils.getRandomInt() >> pollution
        pollutionService.getPollutionDescription() == expectedLevel

        where:
        pollution   || expectedLevel
        1           || PollutionLevel.GOOD
        51          || PollutionLevel.MODERATE
        101         || PollutionLevel.USG
        151         || PollutionLevel.UNHEALTHY
    }

    def "Restart pollution sets empty stats"() {
        given:
        pollutionService.trackPollution()

        when:
        pollutionService.restartPollutionTracker()

        then:
        randomUtils.randomInt >> 1
        pollutionService.getPollutionDescription() == null
    }

}
