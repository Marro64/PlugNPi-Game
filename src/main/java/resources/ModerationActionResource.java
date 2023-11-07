package resources;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.ModerationActionDao;
import dao.ScoreDao;
import dao.SessionDao;
import dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.*;

@Path("/actionlog")
public class ModerationActionResource {
    @Context
    private HttpServletRequest req;

    public static void createAction(int adminID, int affectedID, ModerationType action) {
        ModerationAction mod = new ModerationAction(adminID,affectedID,action);
        ModerationActionDao.INSTANCE.addLog(mod);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogsForUser(@QueryParam("username") String username)
    {
        JsonArray log = new JsonArray();
        JsonObject jsonUser = UserDao.INSTANCE.getByUsername(username);
        User user = new User();
        UserDao.INSTANCE.jsonToUser(jsonUser, user);
        if (UserDao.INSTANCE.isAdmin(user) && UserDao.INSTANCE.UserExists(user))
        {
            System.out.println("MODRES: USER IS ADMIN");
            log = ModerationActionDao.INSTANCE.getLogsFromAdmin(user);
            log.addAll(ModerationActionDao.INSTANCE.getLogsFromUser(user));
        }else {
            System.out.println("MODRES: NO CONTENT");
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(log.toString()).build();
    }

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogsForMe()
    {
        JsonArray log = new JsonArray();
        User user = (User) req.getAttribute("user");
        log = ModerationActionDao.INSTANCE.getLogsFromAdmin(user);
        log.addAll(ModerationActionDao.INSTANCE.getLogsFromUser(user));
        ModerationActionResource.createAction(user.getUid(), user.getUid(), ModerationType.GDPR_DATA_REQUEST); //Getting my own data
        return Response.ok(log.toString()).build();
    }

    @Path("/logs")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLogs()
    {
        JsonArray allLogs = ModerationActionDao.INSTANCE.getAllLogs();
        if(!allLogs.isJsonNull()) {
            return Response.ok(allLogs.toString()).build();
        }else{
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }


}
