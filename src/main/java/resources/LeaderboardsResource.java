package resources;

import com.google.gson.JsonArray;
import dao.ScoreDao;
import dao.SessionDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.PiScore;
import model.Score;

@Path("/leaderboard")
public class LeaderboardsResource {

    @Context
    private HttpServletRequest req;

    /**
     * Have different filters for date, return
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScores(@QueryParam("date") String time) {
        JsonArray allScores = new JsonArray();
        if(time.equals("daily")) {
            allScores = ScoreDao.INSTANCE.getTopLast24();
        } else if (time.equals("daily-m")) {
            //TODO MAURICIO QUERY
        } else if (time.equals("weekly")) {
            allScores = ScoreDao.INSTANCE.getTopLastWeek();
        } else if (time.equals("all-time")) {
            allScores = ScoreDao.INSTANCE.getAllScores();
        }
        return Response.ok(allScores.toString()).build();
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
