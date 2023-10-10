package resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/leaderboard")
public class LeaderboardsResource {

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


}
