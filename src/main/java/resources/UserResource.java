package resources;

import com.google.gson.JsonObject;
import dao.SessionDao;
import dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.*;
import model.ModerationType;
import model.SecurityFactory;
import model.User;
import model.UserType;

import java.net.URI;

@Path("/user")
public class UserResource {

    @Context
    private UriInfo info;

    @Context
    private HttpServletRequest request;

    private String username;

    public UserResource(UriInfo info, HttpServletRequest request, String username) {
        this.info = info;
        this.request = request;
        this.username = username;
    }

    public UserResource() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserDetails() {
        User user = (User) request.getAttribute("user");
        return user;
    }

    /**
     * Admin perms needed for this function
     *
     * @return
     */
    @Path("/permissions")
    @PUT
    public Response switchPermissions() {
        User admin = (User) request.getAttribute("user");
        User userAffected = new User();
        JsonObject jsonObject = UserDao.INSTANCE.getByUsername(username);
        UserDao.INSTANCE.jsonToUser(jsonObject, userAffected);
        if (admin.getUser_type().equals(UserType.ADMIN)) {
            UserDao.INSTANCE.changeUserRole(userAffected);
            ModerationActionResource.createAction(admin.getUid(),userAffected.getUid(),ModerationType.SWITCH_ROLE);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build(); //Used to handle invalid actions (check js)
        }
    }

    /**
     * For GDPR requests, doesn't need any ADMIN perms
     * @return
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser() {
        User user = getUserDetails();
        if (UserDao.INSTANCE.UserExists(user)) {
            String session = (String) request.getAttribute("session");

            UserDao.INSTANCE.deleteUser(user);

            NewCookie cookie = new NewCookie("sessionId", session, "/", null, null, 0, false);
            return Response.ok().cookie(cookie).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

    }

    /**
     * Update settings for a given id
     *
     * @param newSettings
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User newSettings) {
        User oldSettings = (User) request.getAttribute("user");
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

    @Path("/ban")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deActivateUser() {
        User user = new User();
        JsonObject jsonObject = UserDao.INSTANCE.getByUsername(username);
        UserDao.INSTANCE.jsonToUser(jsonObject, user);
        User currentUser = (User) request.getAttribute("user");
        if (currentUser.getUser_type().equals(UserType.ADMIN)) {
            UserDao.INSTANCE.de_Activate(user);
            ModerationActionResource.createAction(currentUser.getUid(),user.getUid(),ModerationType.BAN);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

    }
}
