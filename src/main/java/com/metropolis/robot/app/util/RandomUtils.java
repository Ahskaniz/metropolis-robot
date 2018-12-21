package com.metropolis.robot.app.util;

import java.util.Random;

public class RandomUtils {

    public int getRandomInt() {
        return new Random().nextInt(200);
    }
}
