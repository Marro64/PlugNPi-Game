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

import java.sql.SQLException;
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
    public String createAccount(User user) {
       // int u_id = (int) requestContext.getProperty("u_id");
           // if(UserDao.INSTANCE.UserExists(user.getUsername()))
            {

            }
            return " ";
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
