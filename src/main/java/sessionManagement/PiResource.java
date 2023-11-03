package sessionManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonElement;
import dao.SessionDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.ConnectionRequest;
import model.User;

import javax.print.attribute.standard.Media;
import java.net.URI;
import java.util.HashMap;
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

    /**
     * The user scans a QR code/enters code and makes a request to the server to link the account to the pi.
     * They're already logged in, so we should be taking the user object based on the session_id of the cookie.
     * Requests by users who are not logged in are not accepted, and they're forced to log in.
     * User is guaranteed to exist because they have already managed to log in.
     * //TODO the pi has to know who is playing
     */
    @GET
    @Path("/link")
    public Response connectAccount(@QueryParam("session") String session, @QueryParam("action") String action) {
        User user = (User) httpreq.getAttribute("user");
        if (SessionDao.INSTANCE.getSessions().containsKey(session)) {
            if (action.equals("connect")) {
                if (SessionDao.INSTANCE.getSessions().get(session) == -1) {
                    if (user == null) {
                        // User is not logged in, store the API call data and redirect to login page
                        System.out.println("Attempting to connect " + session + " to user account, but user is not logged in.");
                        System.out.println("Responding with a redirect to login page.");
                        return Response.seeOther(URI.create("/plugnpi/login.html?fromQR=" + session))
                                .build();
                    }
                    System.out.println("Connecting " + session + " to user account " + user.getUid());
                    System.out.println("Responding with a redirect to the leaderboard.");
                    SessionDao.INSTANCE.addPiSession(session, user.getUid());
                    return Response.seeOther(URI.create("/plugnpi/leaderboard.html")).build();
                } else if (SessionDao.INSTANCE.getSessions().get(session) > -1) {
                    System.out.println("Session " + session + " already has a user connected.");
                    System.out.println("Responding with a redirect to the leaderboard.");
                    return Response.seeOther(URI.create("/plugnpi/leaderboard.html")).build();
                } else {
                    System.out.println("Session " + session + " is invalid or corrupted.");
                    System.out.println("Responding 500.");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }

            } else if (action.equals("disconnect")) {
                System.out.println("Disconnecting session " + session);
                System.out.println("Responding with a redirect to the leaderboards.");
                SessionDao.INSTANCE.resetSession(session);
                return Response.seeOther(URI.create("/plugnpi/leaderboard.html")).build();
            } else if (action.equals("request_user")) {
                System.out.println("Request for user connected to " + session);
                System.out.println("Responding with 200 and username.");
                return Response.status(Response.Status.OK).entity("username: "+user.getUsername()).build();
            } else {
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
