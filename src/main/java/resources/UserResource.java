package resources;

import com.google.gson.JsonObject;
import dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import model.SecurityFactory;
import model.User;
import model.UserType;

@Path("/user")
public class UserResource {

    @Context
    private UriInfo info;

    @Context
    private HttpServletRequest req;

    private final String username;

    public UserResource(UriInfo info, HttpServletRequest req, String username) {
        this.info = info;
        this.req = req;
        this.username = username;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserDetails() {
        User user = null;
        //User user = UserDao.instance.getUsers().get(Integer.parseInt(email));
        //String password = uQueries.getPassword(email);
        //user.setPassword(password);
        if (user == null) {
            throw new RuntimeException("User not found");
        } else {
            return user;
        }
    }

    /**
     * Admin perms needed for this function
     *
     * @return
     */
    @Path("/permissions")
    @PUT
    public Response switchPermissions() {
//        User admin = (User) req.getAttribute("user");
        User userAffected = new User();
        JsonObject jsonObject = UserDao.INSTANCE.getByUsername(username);
        UserDao.INSTANCE.jsonToUser(jsonObject, userAffected);
        if (true) {
            UserDao.INSTANCE.changeUserRole(userAffected);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build(); //Used to handle invalid actions (check js)
        }
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUser() {
        User user = new User();
        System.out.println(username);
        JsonObject jsonObject = UserDao.INSTANCE.getByUsername(username);
        UserDao.INSTANCE.jsonToUser(jsonObject, user);
        System.out.println(user.getUsername());
        if (UserDao.INSTANCE.UserExists(user)) {
            UserDao.INSTANCE.deleteUser(user);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

    }

    /**
     * Update settings for a given id
     * @param newSettings
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User newSettings) {
        User oldSettings = new User();
        JsonObject jsonObject = UserDao.INSTANCE.getByUsername(username); //change this to req later
        UserDao.INSTANCE.jsonToUser(jsonObject, oldSettings);
        newSettings.setU_id(oldSettings.getUid());
        String hashed = SecurityFactory.createPassword(newSettings.getPassword());
        newSettings.setPassword(hashed);
        if (UserDao.INSTANCE.UserExists(oldSettings)) {
            UserDao.INSTANCE.updateUser(newSettings);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
