package rest;

import services.AccountService;

import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by gantz on 28.02.16.
 */
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
        // TODO
        final String sessionId = request.getSession().getId();
        final JsonObject result = Json.createObjectBuilder()
                .add("session", sessionId)
                .build();
        return Response.status(Response.Status.NOT_IMPLEMENTED).entity(result).build();
//        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin() {
        // TODO
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogout() {
        // TODO
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

}
