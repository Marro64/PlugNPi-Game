package resources;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    public List<Integer> getScores(@QueryParam("date") String time) {
        //TODO create query for all scores
        List<Integer> allScores = new ArrayList<>();
        if(time.equals("daily")) {
            allScores = null;
        } else if (time.equals("weekly")) {
            allScores = null;
        } else if (time.equals("yearly")) {
            allScores = null;
        } else if (time.equals("all-time")) {
            allScores = null;
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
        //TODO convert into score object
        String pid = score.getPid();
        int uid = SessionDao.INSTANCE.getSessions().get(pid);
        Score dbScore = new Score(uid,score.getDistance());
        ScoreDao.INSTANCE.addScore(dbScore);
        return Response.ok().build();
    }


}
