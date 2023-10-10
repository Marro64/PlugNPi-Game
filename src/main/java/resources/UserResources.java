package resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import model.User;

import java.util.ArrayList;
import java.util.List;

@Path("/users")
public class UserResources {

    @Context
    UriInfo uriInfo;

    @Context
    private HttpServletRequest request;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createAccount(User user) {
        //TODO HASH PASSWORD
        //TODO Create query for users
        return "Successfully created user";
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
