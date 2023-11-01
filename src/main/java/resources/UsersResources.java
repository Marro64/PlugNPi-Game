package resources;

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
    public List<User> getUsers(@QueryParam("role") String role) {
        List<User> users = new ArrayList<>();
        boolean creator = false;
        if (role == null) {
            //return all users
            //users = uQueries.getUsers();
        } else if (role.equals("users")) {
            //return all normal users
            //users = uQueries.getNormals();
        } else if (role.equals("admins")) {
            //return all admins
            //users = uQueries.getAdmins();
        }
        return users;
    }




    @Path("/{username}")
    public UserResource getUser(@PathParam("username") String id) {
        return new UserResource(uriInfo, request, id);
    }
}
