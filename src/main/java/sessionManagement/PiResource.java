package sessionManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonElement;
import dao.SessionDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;

import javax.print.attribute.standard.Media;
import java.util.HashMap;
import java.util.UUID;

/**
 * To handle communication between Pi and Server
 */
@Path("/pi")
public class PiResource {

    @Context
    private HttpServletRequest req;

    /**
     * Have the Pi send a GET request and receive an ID
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String connect() {
        String id = UUID.randomUUID().toString();
        HashMap<String, Integer> sessions = SessionDao.INSTANCE.getSessions();
        while (sessions.containsKey(id)) {
            id = UUID.randomUUID().toString();
        }
        SessionDao.INSTANCE.addPiSession(id,-1); //When you want to connect check whether the id exists, and update the uid
        return id;
    }

    /**
     * The user scans a QR code/enters code and makes a request to the server to link the account to the pi.
     * They're already logged in, so we should be taking the user object based on the session_id of the cookie.
     * Requests by users who are not logged in are not accepted, and they're forced to log in.
     * User is guaranteed to exist because they have already managed to log in.
     * //TODO the pi has to know who is playing
     */
//    @POST
//    @Consumes(MediaType.TEXT_PLAIN)
//    public Response connectAccount(@JsonProperty String session, @JsonProperty boolean connect) {
//        User user = (User) req.getAttribute("user");
//        if(SessionDao.INSTANCE.getSessions().containsKey(session) && SessionDao.INSTANCE.getSessions().get(session)==-1 && connect) { //Pi must already exist, no one should be connected
//            SessionDao.INSTANCE.addSession(session,user.getUid());
//            return Response.ok(user).build(); //Return the user object to the pi !!! TODO Should we also return the same session?
//        }else if(SessionDao.INSTANCE.getSessions().containsKey(session) && !connect) { //TODO make it disconnect after a while
//            SessionDao.INSTANCE.deleteSession(session);
//            return Response.ok().build();
//        }
//        return Response.status(Response.Status.NOT_FOUND).build();
//    }


}
