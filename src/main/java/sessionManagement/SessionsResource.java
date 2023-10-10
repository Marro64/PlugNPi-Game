package sessionManagement;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import model.User;

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
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response tryLogin(User user) {
        System.out.println("Login for: " + user.getEmail());
        //Takes user object which already has an auto hasher
        String hashedPassword = user.getPassword(); //TODO check if this matches up
        boolean login = true; //TODO have query that validates login and takes password as argument
        if(login) {
            //String sessionId = CookieMaker.generateSession(); //generate id
            LocalDateTime expiry = LocalDateTime.now().plusHours(3); //set time
            //sQueries.createSession(sessionId,user.getEmail(),expiry); //TODO Insert session into DB
            //User loggedIn = uQueries.getSingleUser(user.getEmail()); //Also return whether the user is an admin or not
            User loggedIn = new User();
            //NewCookie cookie = CookieMaker.createSession(sessionId,expiry);
            NewCookie cookie = CreateCookie.createSession("test",expiry);
            return Response.ok(loggedIn).cookie(cookie).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
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
            //sQueries.deleteSession(session); //Remove session from DB
            NewCookie cookie = new NewCookie("sessionId", session, "/", null, null, 0, false);
            System.out.println("logout cookie");
            return Response.ok().cookie(cookie).build();
        }else {
            System.out.println("NOT LOGGED IN");
            return Response.ok(Response.Status.UNAUTHORIZED).build(); //Can't log out if you aren't logged in
        }
    }
}
