package com.hdamarcelo.application.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Defines common configurations for REST services
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AbstractRestController {

    protected static final int STATUS_INTERNAL_SERVER_ERROR = 500;
    protected static final int STATUS_OK = 200;
    protected static final int STATUS_CREATED = 201;
    protected static final int STATUS_NO_CONTENT = 204;
    protected static final int STATUS_NOT_FOUND = 404;
    protected static final int STATUS_CONFLICT = 409;

}
