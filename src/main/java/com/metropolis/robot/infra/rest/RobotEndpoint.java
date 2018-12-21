package com.metropolis.robot.infra.rest;

import com.metropolis.robot.app.usecase.RobotUseCase;
import com.metropolis.robot.domain.Report;
import com.metropolis.robot.infra.rest.dto.RunDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/robot")
@Produces(MediaType.APPLICATION_JSON)
public class RobotEndpoint {

    private final RobotUseCase robotUseCase;

    public RobotEndpoint() {
        this.robotUseCase = new RobotUseCase();
    }

    @GET
    @Path("report")
    public Report report() {
        return robotUseCase.report();
    }

    @PUT
    @Path("{action}")
    public void doAction(@PathParam("action") String action) {
        robotUseCase.doAction(action);
    }

    @POST
    @Path("run")
    public void reroute(RunDto route) {
        robotUseCase.run(route.getRoute(), route.getVelocity());
    }

    @PUT
    @Path("disconnect")
    public void disconnect() {
        robotUseCase.disconnect();
    }


}
