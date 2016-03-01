package rest;

import services.AccountService;
import models.ForbiddenResponse;
import models.UserProfile;
import services.AuthenticationService;

import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;


@Singleton
@Path("/user")
public class Users {
    private AccountService accountService;
    private AuthenticationService authService;

    public Users(AccountService accountService, AuthenticationService authService) {
        this.accountService = accountService;
        this.authService = authService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        final Collection<UserProfile> allUsers = accountService.getAllUsers();
        return Response.status(Response.Status.OK).entity(allUsers.toArray(new UserProfile[allUsers.size()])).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserProfile user, @Context HttpHeaders headers) {
        user.setId();
        if (accountService.addUser(user)) {
            final JsonObject result = Json.createObjectBuilder()
                    .add("id", user.getId()).build();
            return Response.status(Response.Status.OK).entity(result).build();
        } else {
            final JsonObject result = Json.createObjectBuilder()
                    .add("message", "login exist")
                    .build();
            return Response.status(Response.Status.FORBIDDEN).entity(result).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") long id) {
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
    public Response deleteUserById(@PathParam("id") long id) {
        if (accountService.removeUser(id)) {
            return Response.status(Response.Status.OK).entity(Json.createObjectBuilder().build()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(new ForbiddenResponse()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") long id, UserProfile userProfile) {
        if (accountService.updateUser(id, userProfile)) {
            final JsonObject result = Json.createObjectBuilder()
                    .add("id", id)
                    .build();
            return Response.status(Response.Status.OK).entity(result).build();
        } else {
            return Response.status(Response.Status.OK).entity(new ForbiddenResponse()).build();
        }
    }


}
