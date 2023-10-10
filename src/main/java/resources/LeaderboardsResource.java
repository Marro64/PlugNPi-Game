package resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadAScore(Score score) {
        //Verify whether score upload matches up with the session
        User user = new User(); //TODO fetch user from sessionId
        if(user.getUid() == score.getUid()) {
            //UID matches up with the session
            //Now upload score
            //TODO query for uploading score
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }


}
