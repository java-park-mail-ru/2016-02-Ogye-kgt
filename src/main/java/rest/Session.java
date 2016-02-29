package rest;

import main.AccountService;

import javax.inject.Singleton;
import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
        return Response.status(Response.Status.OK).entity(Json.createObjectBuilder().add("session", request.getSession().getId()).build()).build();
//        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin() {
        // TODO
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

}
