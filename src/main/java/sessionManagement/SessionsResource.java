package sessionManagement;

import com.google.gson.JsonObject;
import dao.SessionDao;
import dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import model.SecurityFactory;
import model.User;
import model.UserLogin;

import java.time.LocalDateTime;

@Path("/session")
public class SessionsResource {

    @Context
    private HttpServletRequest httprequest;

    //UserQueries uQueries = new UserQueries();
    //SessionQueries sQueries = new SessionQueries();
    /**
     * Gets a password, hashes it and then compares it to whatever is stored in the database.
     *
     * @return the response
     */
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response tryLogin(UserLogin user) {
        System.out.println("Login for: " + user.getUsername());
        String passwordToCheck = user.getPassword(); //this is a normal input, needs to be hashed and salted
        JsonObject dbUser = UserDao.INSTANCE.getByUsername(user.getUsername()); //get the user from the DB
        User userObj = new User();
        UserDao.INSTANCE.jsonToUser(dbUser,userObj);

        if(dbUser != null) {
            int uid = userObj.getUid();
            String pw = userObj.getPassword(); //Password stored in the DB
            String salt = SecurityFactory.getSalt(pw);
            boolean login = SecurityFactory.encryptPassword(passwordToCheck, salt).equals(pw);
            if (login) {
                System.out.println("SessionsResource: logging in");
                String sessionId = CreateCookie.generateSession(); //generate id
                LocalDateTime expiry = LocalDateTime.now().plusHours(3); //set time
                SessionDao.INSTANCE.addSession(sessionId, uid);
                User loggedIn = new User();
                NewCookie cookie = CreateCookie.createSession(sessionId, expiry);
                return Response.ok(uid).cookie(cookie).build();
            }
            System.out.println("SessionResource: incorrect password");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {
            System.out.println("SessionsResource: user not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Get the permissions for this user during this session.
     *
     * @return the user of this permission
     */
    @Path("/permission")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getSessionPermission() {
        return (User) httprequest.getAttribute("user");
    }

    /**
     * TODO check whether session exists
     * Can only be done by users with a valid session.
     *
     * @return the response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response tryLogout() {
        System.out.println("LOGOUT");
        String session = (String) httprequest.getAttribute("session");
        System.out.println("SESSION: " + session);
        if(session != null) {
            SessionDao.INSTANCE.deleteSession(session); //Remove session from DB
            NewCookie cookie = new NewCookie("sessionId", session, "/", null, null, 0, false);
            System.out.println("logout cookie");
            return Response.ok().cookie(cookie).build(); //cancels out the existing cookie
        }else {
            System.out.println("NOT LOGGED IN");
            return Response.ok(Response.Status.UNAUTHORIZED).build(); //Can't log out if you aren't logged in
        }
    }


}
