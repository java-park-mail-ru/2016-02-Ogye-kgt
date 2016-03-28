package rest;

import models.ForbiddenResponse;
import models.UserProfile;
import services.AccountService;
import services.AccountServiceImpl;
import services.AccountServiceImpl.InvalidUserException;
import services.AccountServiceImpl.UserExistsException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;


@Singleton
@Path("/user")
public class Users {
    @Inject
    private main.Context context;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        final AccountService accountService = context.get(AccountService.class);
        final Collection<UserProfile> allUsers = accountService.getAllUsers();
        return Response.status(Response.Status.OK).entity(allUsers.toArray(new UserProfile[allUsers.size()])).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserProfile user, @Context HttpHeaders headers) {
        final AccountService accountService = context.get(AccountService.class);
        try {
            accountService.addUser(user);
            final JsonObject result = Json.createObjectBuilder()
                    .add("id", user.getId()).build();
            return Response.status(Response.Status.OK).entity(result).build();
        } catch (UserExistsException e) {
            final JsonObject result = Json.createObjectBuilder()
                    .add("message", "This user already exist.")
                    .build();
            return Response.status(Response.Status.FORBIDDEN).entity(result).build();
        } catch (InvalidUserException e) {
            final JsonObject result = Json.createObjectBuilder()
                    .add("message", "Invalid user data.")
                    .build();
            return Response.status(Response.Status.FORBIDDEN).entity(result).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") long id) {
        final AccountService accountService = context.get(AccountService.class);
        final UserProfile user = accountService.getUser(id);
        if (user == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            final JsonObject result = Json.createObjectBuilder()
                    .add("id", id)
                    .add("login", user.getLogin())
                    .add("email", user.getEmail())
                    .build();
            return Response.status(Response.Status.OK).entity(result).build();
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
