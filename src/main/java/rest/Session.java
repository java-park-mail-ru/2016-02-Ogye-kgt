package rest;

import models.UserLoginRequest;
import services.AccountService;

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

    public Session(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAuth(@Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        JsonObject result = Json.createObjectBuilder().build();
        if (accountService.isAuthorised(sessionId)) {
            result = Json.createObjectBuilder()
                    .add("id", accountService.getUserBySession(sessionId).getId())
                    .build();
            return Response.status(Response.Status.OK).entity(result).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity(result).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(UserLoginRequest userLoginRequest, @Context HttpServletRequest request) {
        JsonObject result = Json.createObjectBuilder().build();
        final String sessionId = request.getSession().getId();
        if (accountService.doLogin(sessionId, userLoginRequest)) {
            //noinspection ConstantConditions
            final long userId = accountService.getUserByLogin(userLoginRequest.getLogin()).getId();
            result = Json.createObjectBuilder()
                    .add("id", userId)
                    .build();
            Response.status(Response.Status.OK).entity(result).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(result).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogout(@Context HttpServletRequest request) {
        final String sessionId = request.getSession().getId();
        accountService.doLogout(sessionId);
        final JsonObject result = Json.createObjectBuilder().build();
        return Response.status(Response.Status.OK).entity(result).build();
    }

}
