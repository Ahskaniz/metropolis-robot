package com.metropolis.robot.app.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.serverError()
                .entity(new MetropolisNotFoundException(exception.getMessage()))
                .build();
    }

    private class MetropolisNotFoundException {

        @JsonProperty
        private String details;

        @JsonCreator
        MetropolisNotFoundException(@JsonProperty("details") String details) {
            this.details = details;
        }

        @JsonProperty
        public String getDetails() {
            return details;
        }

        @JsonProperty
        public void setDetails(String details) {
            this.details = details;
        }
    }
}
