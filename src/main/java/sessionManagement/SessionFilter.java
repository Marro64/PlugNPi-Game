package sessionManagement;

import com.google.gson.JsonObject;
import dao.SessionDao;
import dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.Provider;
import model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Provider
public class SessionFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest httprequest;

    private final String COOKIE_SESSION = "sessionId";

    //SessionQueries queries = new SessionQueries();

    /**
     * Filter out incoming requests if they don't have a valid session id
     * If successful, the program should continue handling the restful services
     * @param request incoming request
     */
    @Override
    public void filter(ContainerRequestContext request) {
        System.out.println("new request!");
        Cookie session = request.getCookies().get(COOKIE_SESSION);

        //TESTING BLOCK FOR API CALLS: REMOVE WHEN YOU WANT TO TEST SESSIONS
//        if (true) {
//            return;
//        }

        if (session == null) { //No session, user wants to login or signup
            System.out.println("SessionFilter: No session");
            System.out.println("SessionFilter " + request.getUriInfo().getRequestUri().getPath());
            if (request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/api/session/login") || request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/api/users/") || request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/api/pi")) {
                if (request.getMethod().equals("POST") || request.getMethod().equals("GET")) {
                    return; // Allow the request to continue processing
                }
            }
            Response unauthorized = Response.status(Response.Status.UNAUTHORIZED).entity("Accessing function without login").build(); //Trying to access page wihout being logged in
            request.abortWith(unauthorized);
            return;
        } else if (verifySession(session.getValue())) { //Valid session to any destination but not login
            //Session is still valid
            httprequest.setAttribute("session",session.getValue());
            if(!request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/api/session/login")) {
                JsonObject queried = UserDao.INSTANCE.getUserFromSession(session.getValue());
                User user = new User();
                UserDao.INSTANCE.jsonToUser(queried,user);
                System.out.println("SessionFilter: SESSION: " + user.getUsername());
                httprequest.setAttribute("user", user);
            } else if (request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/api/session/logout")) {
                httprequest.setAttribute("session", session.getValue());
            } else {
                //TODO remove session on DB
                SessionDao.INSTANCE.deleteSession(session.getValue()); //Removes existing cookie if the user wants to login again (which will give them a new cookie)
            }
            return; // Allow the request to continue processing
        } else if (session != null && (request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/api/session/login") || (request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/api/users/")))) { //Session already there but to login/signup
            //Previous check guarantees that an invalid session is removed
            if (request.getMethod().equals("POST")) {
                System.out.println("SessionFilter: login/signup attempt on existing cookie");
                return; // Allow new login attempt
            }
        }
        Response unauthorized = Response.status(Response.Status.UNAUTHORIZED).entity("Edge case, not valid.").build();
        request.abortWith(unauthorized);
    }


    /**
     * Check if the session id is still valid in terms of expiry date.
     *
     * @return true if session is valid
     */
    public boolean verifySession(String session) {
        System.out.println("SessionFilter: checking session");

        String sessionTimeString = SessionDao.INSTANCE.getSessiontime(session).get("expires").getAsString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH);
        LocalDateTime sessionTime = LocalDateTime.parse(sessionTimeString, formatter);

        LocalDateTime now = LocalDateTime.now();
        if(sessionTime.isAfter(now)) {
            System.out.println("SessionFilter: valid sessionid");
            return true;
        } else if (!sessionTime.isAfter(now)) {
            System.out.println("SessionFilter: Expired session id");
            SessionDao.INSTANCE.deleteSession(session);
        }
        return false;
    }

}
