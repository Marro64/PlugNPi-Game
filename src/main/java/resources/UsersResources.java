package resources;

import com.google.gson.JsonArray;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import model.User;
import dao.UserDao;
import model.UserLogin;
import model.UserSignup;
import model.UserType;

import java.util.ArrayList;
import java.util.List;

@Path("/users")
public class UsersResources {

    @Context
    UriInfo uriInfo;

    @Context
    private ContainerRequestContext requestContext;

    @Context
    private HttpServletRequest request;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(UserSignup user) {
        System.out.println(user.getPassword());
        System.out.println(user.getUsername());
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        if(UserDao.INSTANCE.UserExists(newUser)) {
            System.out.println("user exists");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }else {
            UserDao.INSTANCE.addUser(user);
            System.out.println("created new user");
            return Response.ok().build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@QueryParam("role") String role) {
        JsonArray users = new JsonArray();
        if (role.equals("undefined")) {
            users = UserDao.INSTANCE.getAllUsers(role);
        } else if (role.equals("PLAYER")) {
            users = UserDao.INSTANCE.getAllUsers(role);
        } else if (role.equals("ADMIN")) {
            users = UserDao.INSTANCE.getAllUsers(role);
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(users.toString()).build();
    }

    @Path("/{username}")
    public UserResource getUser(@PathParam("username") String id) {
        return new UserResource(uriInfo, request, id);
    }

    @GET
    @Path("/check")
    public boolean isAdmin() {
        User user = (User) request.getAttribute("user");
        if(user.getUser_type().equals(UserType.ADMIN)) {
            return true;
        } else {
            return false;
        }
    }
}
