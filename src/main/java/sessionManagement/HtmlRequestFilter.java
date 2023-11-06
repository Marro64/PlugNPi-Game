//package sessionManagement;
//import jakarta.ws.rs.container.ContainerRequestContext;
//import jakarta.ws.rs.container.ContainerRequestFilter;
//import jakarta.ws.rs.core.Cookie;
//import jakarta.ws.rs.core.Response;
//import jakarta.ws.rs.ext.Provider;
//
//import java.io.IOException;
//import java.net.URI;
//import java.util.Arrays;
//import java.util.List;
//
//import static sessionManagement.SessionFilter.verifySession;
//
//@Provider
//public class HtmlRequestFilter implements ContainerRequestFilter {
//
//    private final String COOKIE_SESSION = "sessionId";
//
//    private enum filterType {
//        NORMAL,REDIRECT_LOGIN,REDIRECT_HOME,NOTFOUND
//    }
//
//    @Override
//    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
//        Cookie session = containerRequestContext.getCookies().get(COOKIE_SESSION);
//        String uri = containerRequestContext.getUriInfo().getRequestUri().getPath();
//        System.out.println("HTMLFILTER: " + uri);
//        String method = containerRequestContext.getMethod();
//        filterType toHandle = null;
//
//        if(validRequest(uri)) {
//            if(session != null) {
//                toHandle = userLoggedInHandler(session.getValue(), uri, method);
//            } else {
//                toHandle = filterType.REDIRECT_LOGIN;
//            }
//        } else {
//            toHandle = filterType.NOTFOUND;
//        }
//
//        switch (toHandle) {
//            case REDIRECT_LOGIN:
//                Response redirectLogin = Response.seeOther(URI.create("/plugnpi/login.html")).build();
//                containerRequestContext.abortWith(redirectLogin);
//            case NORMAL:
//                return;
//            case REDIRECT_HOME:
//                Response redirectHome = Response.seeOther(URI.create("/plugnpi/leaderboard.html")).build();
//                containerRequestContext.abortWith(redirectHome);
//            default: //For API calls
//                System.out.println("HTMLFILTER: UNHANDLED CASE");
//        }
//    }
//
//    public filterType userLoggedInHandler (String session, String uri, String method) {
//        boolean valid = verifySession(session); //Will delete invalid session
//        if (valid) { //Unexpired
//            if(uri.equals("/plugnpi/login.html") || uri.equals("/plugnpi/signup.html")) {
//                return filterType.REDIRECT_HOME;
//            }
//            return filterType.NORMAL;
//        } else { //Expired
//            return filterType.REDIRECT_LOGIN;
//        }
//    }
//
//    public boolean validRequest(String uri) {
//        List<String> allowedHTMLFiles = Arrays.asList(
//                "CP.html",
//                "demo.html",
//                "index.html",
//                "leaderboard.html",
//                "login.html",
//                "mauriciosLeaderboard.html",
//                "notfound.html",
//                "play.html",
//                "PP.html",
//                "signup.html",
//                "TOS.html");
//
//        if (allowedHTMLFiles.contains(uri)) {
//            return true;
//        } else {
//            return  false;
//        }
//    }
//
//    public static void main(String[] args) {
//        String uri = "http://localhost:8080/plugnpi/login.html";
//        String requestedFileName = uri.substring(uri.lastIndexOf("/") + 1);
//        System.out.println(requestedFileName);
//    }
//}
//
