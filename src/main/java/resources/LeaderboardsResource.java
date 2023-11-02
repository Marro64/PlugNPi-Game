package resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dao.ScoreDao;
import dao.SessionDao;
import dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.PiScore;
import model.Score;
import model.User;

import java.util.ArrayList;
import java.util.List;

@Path("/leaderboard")
public class LeaderboardsResource {

    @Context
    private HttpServletRequest req;

    /**
     * Have different filters for date, return
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getScores(@QueryParam("date") String time) {
        JsonArray allScores = new JsonArray();
        if (time.equals("daily")) {
            int size = ScoreDao.INSTANCE.getTopLast24().size();
            while (size > 0) {
                JsonArray array = ScoreDao.INSTANCE.getTopLast24();
                allScores.add(array);
                size--;
            }
        }
        else if (time.equals("daily-m")) {
            //TODO MAURICIO QUERY
        } else if (time.equals("weekly")) {
            int size = ScoreDao.INSTANCE.getTopLastWeek().size();
            while (size > 0) {
                JsonArray array = ScoreDao.INSTANCE.getTopLastWeek();
                allScores.add(array);
                size --;
            }
        } else if (time.equals("all-time")) {
            int size = ScoreDao.INSTANCE.getAllScores().size();
            while (size > 0) {
                JsonArray array = ScoreDao.INSTANCE.getAllScores();
                allScores.add(array);
                size--;
            }
        }
        return allScores;
    }

    /**
     * Upload a piscore from the pi and upload the score to the database
     * @param score a piscore, which has pi and score
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadAScore(PiScore score) {
        //Verify whether score upload matches up with the session
        String pid = score.getPid();
        int uid = SessionDao.INSTANCE.getSessions().get(pid);
        Score dbScore = new Score(uid,score.getDistance());
        ScoreDao.INSTANCE.addScore(dbScore);
        return Response.ok().build();
    }


}
