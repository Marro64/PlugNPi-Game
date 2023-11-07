package sessionManagement;

import com.google.gson.JsonObject;
import dao.SessionDao;
import dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * To handle communication between Pi and Server
 */
@Path("/pi")
public class PiResource {

    @Context
    private HttpServletRequest httpreq;

    /**
     * Have the Pi send a GET request and receive an ID
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String connect() {
        String id = UUID.randomUUID().toString();
        String indicator="session:";
        HashMap<String, Integer> sessions = SessionDao.INSTANCE.getSessions();
        indicator = indicator+id+"\n";
        while (sessions.containsKey(id)) {
            System.out.println("Generating id: " + id);
            id = UUID.randomUUID().toString();
            indicator = indicator+id+"\n";
        }
        System.out.println("Sending: " + indicator);
        SessionDao.INSTANCE.addPiSession(id,-1); //When you want to connect check whether the id exists, and update the uid
        return indicator;
    }

    public void removeSession(int uid) {
        for (Map.Entry<String, Integer> entry : SessionDao.INSTANCE.getSessions().entrySet()) {
            if (entry.getValue() == uid) {
                String sessionId = entry.getKey();
                SessionDao.INSTANCE.removeSession(sessionId);
            }
        }
    }

    /**
     * For the user to join a game
     * @return a 200 OK if the user has joined a session
     */
    @GET
    @Path("/queue")
    public Response queueGame() {
        User user = (User) httpreq.getAttribute("user");
        int uid = user.getUid();

        for (Map.Entry<String, Integer> entry : SessionDao.INSTANCE.getSessions().entrySet()) {
            if (entry.getValue() == uid) {
                String sessionId = entry.getKey();
                SessionDao.INSTANCE.addInGame(sessionId);
                return Response.ok().build();
            }
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public void unQueueGame(int uid) {
        for (Map.Entry<String, Integer> entry : SessionDao.INSTANCE.getSessions().entrySet()) {
            if (entry.getValue() == uid) {
                String sessionId = entry.getKey();
                SessionDao.INSTANCE.removeInGame(sessionId);
            }
        }
    }



    /**
     * The user scans a QR code/enters code and makes a request to the server to link the account to the pi.
     * They're already logged in, so we should be taking the user object based on the session_id of the cookie.
     * Requests by users who are not logged in are not accepted, and they're forced to log in.
     * User is guaranteed to exist because they have already managed to log in.
     */
    @GET
    @Path("/link")
    public Response connectAccount(@QueryParam("session") String session, @QueryParam("action") String action) {
        User user = (User) httpreq.getAttribute("user");
        if (SessionDao.INSTANCE.getSessions().containsKey(session))
        {
            if (action == null || action.equals("connect"))
            {
                if (SessionDao.INSTANCE.getSessions().get(session) == -1)
                {
                    if (user == null)
                    {
                        // User is not logged in, store the API call data and redirect to login page
                        System.out.println("Attempting to connect " + session + " to user account, but user is not logged in.");
                        System.out.println("Responding with a redirect to login page.");
                        return Response.seeOther(URI.create("/plugnpi/login.html?fromQR=" + session))
                                .build();
                    }
                    System.out.println("Connecting " + session + " to user account " + user.getUid());
                    System.out.println("Responding with a redirect to the leaderboard.");
                    removeSession(user.getUid()); //NEW NEEDS TEST
                    SessionDao.INSTANCE.addPiSession(session, user.getUid());
                    return Response.seeOther(URI.create("/plugnpi/leaderboard.html")).build();
                }
                else if (SessionDao.INSTANCE.getSessions().get(session) > -1)
                {
                    System.out.println("Session " + session + " already has a user connected.");
                    System.out.println("Responding with a redirect to the leaderboard.");
                    return Response.seeOther(URI.create("/plugnpi/leaderboard.html")).build();
                }
                else
                {
                    System.out.println("Session " + session + " is invalid or corrupted.");
                    System.out.println("Responding 500.");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }

            }
            else if (action.equals("disconnect"))
            {
                System.out.println("Disconnecting session " + session);
                System.out.println("Responding with a redirect to the leaderboards.");
                SessionDao.INSTANCE.resetSession(session);
                return Response.seeOther(URI.create("/plugnpi/leaderboard.html")).build();
            }
            else if (action.equals("request_user"))
            {
                System.out.println("Request for user connected to " + session);
                int userID = SessionDao.INSTANCE.getSessions().get(session);
                User sesssionUser = new User();
                JsonObject jsonObject = UserDao.INSTANCE.getUser(userID);
                if(jsonObject != null) {
                    UserDao.INSTANCE.jsonToUser(jsonObject, sesssionUser);
                    String output = "username: "+ sesssionUser.getUsername() + "\n";
                    System.out.println("Responding with 200 and " + output);
                    return Response.status(Response.Status.OK).entity(output).build();
                }
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            else if (action.equals("request_join")) { //Queries it's own session with request_join action
                return Response.ok("queued:" + SessionDao.INSTANCE.getInGame().contains(session)).build();
            }
            else
            {
                System.out.println("Invalid request action.");
                System.out.println("Responding with 400.");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        System.out.println("Invalid request, is the session ID valid?");
        System.out.println("Responding 400.");
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
