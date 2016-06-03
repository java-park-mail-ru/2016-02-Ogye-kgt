package rest;

import models.ForbiddenResponse;
import models.UserProfile;
import services.AccountService;
import services.AccountServiceImpl.InvalidUserException;
import services.AccountServiceImpl.DatabaseException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.crypto.Data;
import java.util.Collection;


@Singleton
@Path("/user")
public class Users {
    @Inject
    private main.Context context;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        try {
            final AccountService accountService = context.get(AccountService.class);
            final Collection<UserProfile> allUsers = accountService.getAllUsers();
            return Response.status(Response.Status.OK).entity(allUsers.toArray(new UserProfile[allUsers.size()])).build();
        } catch (DatabaseException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserProfile user, @Context HttpHeaders headers) {
        final AccountService accountService = context.get(AccountService.class);
        if (!user.isValid()) {
            final JsonObject result = Json.createObjectBuilder()
                    .add("message", "Invalid user data.")
                    .build();
            return Response.status(Response.Status.FORBIDDEN).entity(result).build();
        }

        try {
            accountService.addUser(user);
            final JsonObject result = Json.createObjectBuilder()
                    .add("id", user.getId()).build();
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (DatabaseException e) {
            final JsonObject result = Json.createObjectBuilder()
                    .add("message", "This user already exist.")
                    .build();
            return Response.status(Response.Status.FORBIDDEN).entity(result).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") long id) {
        final AccountService accountService = context.get(AccountService.class);
        try {
            final UserProfile user = accountService.getUser(id);
            final JsonObject result = Json.createObjectBuilder()
                    .add("id", id)
                    .add("login", user.getLogin())
                    .build();
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (DatabaseException | NullPointerException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserById(@PathParam("id") long id, @Context HttpServletRequest request) {
        final AccountService accountService = context.get(AccountService.class);
        final String sessionId = request.getSession().getId();
        if (accountService.removeUser(sessionId, id)) {
            return Response.status(Response.Status.OK).entity(Json.createObjectBuilder().build()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(new ForbiddenResponse()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") long id, UserProfile userProfile, @Context HttpServletRequest request) {
        final AccountService accountService = context.get(AccountService.class);
        final String sessionId = request.getSession().getId();
        if (accountService.updateUser(sessionId, id, userProfile)) {
            final JsonObject result = Json.createObjectBuilder()
                    .add("id", id)
                    .build();
            return Response.status(Response.Status.OK).entity(result).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(new ForbiddenResponse()).build();
        }
    }


}
