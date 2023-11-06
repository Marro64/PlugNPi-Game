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
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadAction(ModerationAction action) {
        ModerationActionDao.INSTANCE.addLog(action);
        return Response.ok().build();
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
            log = ModerationActionDao.INSTANCE.getLogsFromAdmin(user);

        } else if (!UserDao.INSTANCE.isAdmin(user) && UserDao.INSTANCE.UserExists(user)) {
            log = ModerationActionDao.INSTANCE.getLogsFromUser(user);
        }else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

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
