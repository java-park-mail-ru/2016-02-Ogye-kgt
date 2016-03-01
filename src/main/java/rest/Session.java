package rest;

import models.UserLoginRequest;
import services.AccountService;
import services.AuthenticationService;

import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Singleton
@Path("/session")
public class Session {
    private AccountService accountService;
    private AuthenticationService authenticationService;

    public Session(AccountService accountService, AuthenticationService authenticationService) {
        this.accountService = accountService;
        this.authenticationService = authenticationService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAuth(@Context HttpServletRequest request) {
        // TODO
        final String sessionId = request.getSession().getId();
        final JsonObject result = Json.createObjectBuilder()
                .add("session", sessionId)
                .build();
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity(result).build();
//        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(UserLoginRequest userLoginRequest, @Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        // TODO
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity(userLoginRequest).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogout() {
        // TODO
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

}
