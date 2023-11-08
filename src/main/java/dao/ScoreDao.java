package dao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import db.ORM;
import jakarta.ws.rs.core.Response;
import model.*;


public enum ScoreDao {
    INSTANCE;

    public int getTopScore(User user)
    {
            JsonArray userQuery = ORM.executeQuery("SELECT a.username, MAX(s.distance)\n" +
                    "FROM account a, score s\n" +
                    "WHERE a.u_id = ? AND s.u_id = a.u_id\n" +
                    "GROUP BY a.u_id" , user.getUid());
        int userId = ((JsonObject) userQuery.get(0)).get("u_id").getAsInt();
        return userId;
    }

    public void addScore(Score score) {
        JsonArray addUserQuerry =  ORM.executeQuery(
                "INSERT INTO project.score " +
                        "(u_id, distance) " +
                        "VALUES (?, ?)",
                score.getUid(),score.getDistance());
    }

    public void deleteScore(int scoreid) {
        JsonArray addUserQuerry =  ORM.executeQuery(
                "DELETE FROM project.score s WHERE s.s_id = ?",
                scoreid);
    }

    public int getUidForScore(int scoreid) {
        JsonArray addUserQuerry =  ORM.executeQuery(
                "SELECT s.u_id FROM project.score s WHERE s.s_id = ?",
                scoreid);
        int userId = ((JsonObject) addUserQuerry.get(0)).get("u_id").getAsInt();
        return userId;
    }


    public JsonArray getTop100AllTime()
    {
      return ORM.executeQuery("SELECT\n" +
                "    a.username,\n" +
                "    s.distance,\n" +
                "    s.date_of_record\n" +
                "FROM project.score s, project.account a\n" +
                "WHERE s.s_id IN (\n" +
                "    SELECT DISTINCT ON (u_id) s_id\n" +
                "    FROM project.score\n" +
                "    ORDER BY u_id, distance DESC)\n" +
                "AND a.active = TRUE\n" +
                "AND s.u_id = a.u_id\n" +
                "ORDER BY distance DESC\n" +
                "LIMIT 100;");

    }
    public JsonArray getTopLast24()
    {
        return  ORM.executeQuery("SELECT\n" +
                "    a.username,\n" +
                "    s.distance,\n" +
                "    s.s_id,\n" +
                "    s.date_of_record\n" +
                "FROM project.score s, project.account a\n" +
                "WHERE s.s_id IN (\n" +
                "    SELECT DISTINCT ON (u_id) s_id\n" +
                "    FROM project.score\n" +
                "    ORDER BY u_id, distance DESC)\n" +
                "AND s.u_id = a.u_id\n" +
                "AND a.active = TRUE\n" +
                "AND date_of_record >= NOW() - INTERVAL '24 hours'\n" +
                "ORDER BY distance DESC\n");

    }
    public JsonArray getAllScores()
    {
        JsonArray objects =  ORM.executeQuery("SELECT\n" +
                "    a.username,\n" +
                "    s.distance,\n" +
                "    s.s_id,\n" +
                "    s.date_of_record\n" +
                "FROM project.score s, project.account a\n" +
                "WHERE s.s_id IN (\n" +
                "    SELECT DISTINCT ON (u_id) s_id\n" +
                "    FROM project.score\n" +
                "    ORDER BY u_id, distance DESC)\n" +
                "AND s.u_id = a.u_id\n" +
                "AND a.active = TRUE\n" +
                "ORDER BY distance DESC\n");
        System.out.println(objects);
        try {
            System.out.println(objects.size());
        }catch (NullPointerException e) {
            System.out.println("LMAO NO ENTRY");
        }
        return objects;
    }

    public JsonArray getMauricioScores() {
        JsonArray objects = ORM.executeQuery("SELECT\n" +
                "    a.username,\n" +
                "    s.distance,\n" +
                "    s.s_id,\n" +
                "    s.date_of_record\n" +
                "FROM project.score s, project.account a\n" +
                "WHERE s.u_id = a.u_id\n" +
                "AND a.active = TRUE\n" +
                "ORDER BY s.distance DESC");
        return objects;
    }

    public JsonArray getTopLastWeek()
    {
        return  ORM.executeQuery("SELECT\n" +
                "    a.username,\n" +
                "    s.distance,\n" +
                "    s.s_id,\n" +
                "    s.date_of_record\n" +
                "FROM project.score s, project.account a\n" +
                "WHERE s.s_id IN (\n" +
                "    SELECT DISTINCT ON (u_id) s_id\n" +
                "    FROM project.score\n" +
                "    ORDER BY u_id, distance DESC)\n" +
                "AND s.u_id = a.u_id\n" +
                "AND a.active = TRUE\n" +
                "AND date_of_record >= NOW() - INTERVAL '7 days'\n" +
                "ORDER BY distance DESC\n");
    }

}

