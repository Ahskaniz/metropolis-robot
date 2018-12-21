package com.metropolis.robot;

import com.metropolis.robot.app.util.ThreadUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Starter {

    public static void main(String args[]) {
        ThreadUtils threadUtils = new ThreadUtils();
        Runnable api = () -> {
            try {
                new RestAPI().run(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Runnable robot = () -> new LondonRobot().run();

        ExecutorService es = Executors.newFixedThreadPool(2);
        System.out.println("Waiting the api to awake...");
        es.submit(api);
        threadUtils.sleep(3000);
        System.out.println("API awake!");
        es.submit(robot);
    }

}
