# ASSUMPTIONS

- If robot is on a route, stoping/starting will interact with the current route.
- Robot reports also when it's stopped.
- Real time for robot running is not taken in consideration on the initial (tested) version. Assuming real time to be computed would mean.
    - Implementation:
        - Thread sleep for the remaining time in between movement speed and next calculation.
        - 15 real minutes for one report.

    If still you want to see the timing being considered this version has a 10msec (instead of 1sec) delay on running time. See [moveUntilNextPoint](src/main/java/com/metropolis/robot/app/service/RobotService.java#181).
- The robot reports once per `MonitoringStation`. More can be added at [LondonRobot](src/main/java/com/metropolis/robot/LondonRobot.java#L20)
- Rerouting counts as a new route from scratch if the robot has not being running, if was already in a route counts as the current position the starting point moving towards new route.

(I left comments along the code to see through command line the path the robot is following)

# CODE STRUCTURE

Rest API and London robot run as separate threads.

API connects to robotService through the use cases defined on [RobotUseCase](src/main/java/com/metropolis/robot/app/usecase/RobotUseCase.java). The use case contains RobotService as a singleton instance.

Reason of being that way is based on making RobotService act as a real autonomous robot.

# COMPILE

```
> ./gradlew robot
```

# RUN

```
> java -jar build/libs/robot-1.0-SNAPSHOT.jar server restAPI.yaml
```

This will wake up a REST API and after 3 seconds the robot.
Interaction with the robot is with the REST Postman collection. Find it at [Metropolis.postman_colleciton](Metropolis.postman_collection.json)

# TESTS

```
> ./gradlew test
```

I've decided to test services with business logic in a use case interaction based.

The fact of the robot being a singleton instance which runs continuosly (to make it more real) makes tests complicated to implement.