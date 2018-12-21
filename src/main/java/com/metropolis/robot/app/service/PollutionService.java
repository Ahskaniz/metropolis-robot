package com.metropolis.robot.app.service;

import com.metropolis.robot.app.util.RandomUtils;
import com.metropolis.robot.domain.PollutionLevel;

public class PollutionService {

    private int checkedPoints = 0;
    private int accumulatedPollution = 0;
    private RandomUtils randomUtils;

    public PollutionService(RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    public void restartPollutionTracker() {
        this.checkedPoints = 0;
        this.accumulatedPollution = 0;
    }

    public void trackPollution() {
        accumulatedPollution += randomUtils.getRandomInt();
        checkedPoints += 1;
    }

    PollutionLevel getPollutionDescription() {
        if (checkedPoints == 0) return null;
        double pollution = (accumulatedPollution/checkedPoints);
        if (pollution <= 50) return PollutionLevel.GOOD;
        else if (pollution <= 100) return PollutionLevel.MODERATE;
        else if (pollution <= 150) return PollutionLevel.USG;
        else return PollutionLevel.UNHEALTHY;
    }

}
