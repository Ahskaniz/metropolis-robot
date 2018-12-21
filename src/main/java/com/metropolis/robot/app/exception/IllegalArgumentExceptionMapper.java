package com.metropolis.robot.app.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response.serverError()
                .entity(new MetropolisIllegalArgumentException(exception.getMessage()))
                .build();
    }

    private class MetropolisIllegalArgumentException {

        @JsonProperty
        private String details;

        @JsonCreator
        MetropolisIllegalArgumentException(@JsonProperty("details") String details) {
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
