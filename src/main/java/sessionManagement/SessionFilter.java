package sessionManagement;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.Provider;
import model.User;

import java.time.LocalDateTime;

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
        if (session == null) {
            return;
        }

        if (session == null) { //No session, user wants to login or signup
            System.out.println("No session");
            System.out.println(request.getUriInfo().getRequestUri().getPath());
            if (request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/session") || request.getUriInfo().getRequestUri().getPath().equals("/actfact/api/users/")) {
                if (request.getMethod().equals("POST")) {
                    return; // Allow the request to continue processing
                }
            }
            Response unauthorized = Response.status(Response.Status.UNAUTHORIZED).entity("Accessing function without login").build(); //Trying to access page wihout being logged in
            request.abortWith(unauthorized);
            return;
        } else if (verifySession(session.getValue())) { //Valid session to any destination but not login
            //Session is still valid
            httprequest.setAttribute("session",session.getValue());
            if(!request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/session")) {
                User user = new User();//TODO Query the user
                System.out.println("SESSION: " + user.getEmail());
                httprequest.setAttribute("user", user);
            } else if (request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/session/logout")) {
                httprequest.setAttribute("session", session.getValue());
            } else {
                //TODO remove session on DB
                //queries.deleteSession(session.getValue()); //Removes existing cookie if the user wants to login again (which will give them a new cookie) TODO REMOVE COOKIE IN JS
            }
            return; // Allow the request to continue processing
        } else if (session != null && (request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/session") || request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/api/users/"))) { //Session already there but to login/signup
            //Previous check guarantees that an invalid session is removed
            if (request.getMethod().equals("POST")) {
                System.out.println("login attempt on existing cookie");
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
        System.out.println("checking session");
        //TODO query session in DB
        //LocalDateTime sessionTime = queries.getSessionTime(session);
        LocalDateTime sessionTime = null;
        System.out.println("finished query");
        LocalDateTime now = LocalDateTime.now();
        if(sessionTime != null  && sessionTime.isAfter(now)) {
            System.out.println("valid sessionid");
            return true;
        } else if (sessionTime != null  && !sessionTime.isAfter(now)) {
            System.out.println("Expired session id");
            //TODO delete session in DB
            //queries.deleteSession(session);
        }
        //TODO make this return the user perms which is used as context for the respective request
        return false;
    }

}
