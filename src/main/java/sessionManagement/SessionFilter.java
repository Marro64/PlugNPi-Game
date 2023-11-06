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

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Provider
public class SessionFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest httprequest;

    private final String COOKIE_SESSION = "sessionId";

    private enum filterType {
        NORMAL,REDIRECT_LOGIN,REDIRECT_HOME, REMOVE_COOKIE
    }

//        } else if (!verifySession(session.getValue()) && request.getUriInfo().getRequestUri().getPath().equals("/plugnpi/api/pi/link")) {
//            return;


    @Override
    public void filter(ContainerRequestContext request) {
        Cookie session = request.getCookies().get(COOKIE_SESSION);
        String uri = request.getUriInfo().getRequestUri().getPath();
        System.out.println("SESSIONFILTER: " + uri);
        String method = request.getMethod();
        filterType toHandle = null;

        if(session==null) {
            toHandle = userNotLoggedInHandler(uri);
        } else {
            String sessionId = session.getValue();
            toHandle = userLoggedInHandler(sessionId,uri,method);
        }

        switch (toHandle) {
            case REDIRECT_LOGIN:
                Response redirectLogin = Response.seeOther(URI.create("/plugnpi/login.html")).build();
                request.abortWith(redirectLogin);
                break;
            case NORMAL:
                return;
            case REDIRECT_HOME:
                Response redirectHome = Response.seeOther(URI.create("/plugnpi/leaderboard.html")).build();
                request.abortWith(redirectHome);
                break;
            case REMOVE_COOKIE:
                NewCookie cookie = new NewCookie("sessionId", session.getValue(), "/", null, null, 0, false);
                System.out.println("logout cookie");
                Response remove = Response.seeOther(URI.create("/plugnpi/login.html")).cookie(cookie).build(); //cancels out the existing cookie
                request.abortWith(remove);
            default:
                System.out.println("SESSIONFILTER: UNHANDLED CASE");
        }
    }

    public void setUser(String session) {
        JsonObject queried = UserDao.INSTANCE.getUserFromSession(session);
        User user = new User();
        UserDao.INSTANCE.jsonToUser(queried, user);
        System.out.println("SessionFilter: SESSION: " + user.getUsername());
        httprequest.setAttribute("session",session);
        httprequest.setAttribute("user", user);
    }

    public filterType userLoggedInHandler (String session, String uri, String method) {
        boolean valid = verifySession(session); //Will delete invalid session
        if (valid) { //Unexpired
            if(uri.equals("/plugnpi/api/session/login") || (uri.equals("/plugnpi/api/session/users") && method.equals("POST"))) {
                return filterType.REDIRECT_HOME;
            }
            setUser(session);
            return filterType.NORMAL;
        } else { //Expired
            return filterType.REMOVE_COOKIE;
        }
    }

    public filterType userNotLoggedInHandler (String uri) {
        switch (uri) {
            case "/plugnpi/api/session/login":
            case "/plugnpi/api/users":
            case "/plugnpi/api/pi":
            case "/plugnpi/api/pi/link":
            case "/plugnpi/api/pi/getUsername":
            case "/plugnpi/api/leaderboard":
                return filterType.NORMAL;
            default:
                return filterType.REDIRECT_LOGIN;
        }
    }


    /**
     * Check if the session id is still valid in terms of expiry date.
     *
     * @return true if session is valid
     */
    public static boolean verifySession(String session) {
        System.out.println("SessionFilter: checking session, " + session);
        if(session != null) {
            System.out.println("SESSIONTIME: " + SessionDao.INSTANCE.getSessiontime(session));
            String sessionTimeString = SessionDao.INSTANCE.getSessiontime(session).get("expires").getAsString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH);
            LocalDateTime sessionTime = LocalDateTime.parse(sessionTimeString, formatter);

            LocalDateTime now = LocalDateTime.now();
            if (sessionTime.isAfter(now)) {
                System.out.println("SessionFilter: valid sessionid");
                return true;
            } else if (!sessionTime.isAfter(now)) {
                System.out.println("SessionFilter: Expired session id");
                SessionDao.INSTANCE.deleteSession(session);
            }
            return false;
        }
        return false;
    }
}
