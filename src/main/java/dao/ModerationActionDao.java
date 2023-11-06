package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.ORM;
import model.ModerationAction;
import model.User;
import model.UserSignup;

public enum ModerationActionDao {
    INSTANCE;

    public int addLog(ModerationAction log)
    {
        JsonArray addLogQuerry = ORM.executeQuery(
                "INSERT INTO project.moderation_action (admin_id, player_id, action_type) VALUES (?, ?, ?)",
                log.getAid(), log.getPid(), log.getActionType()

        );
        int logId = ((JsonObject) addLogQuerry.get(0)).get("log_id").getAsInt();
        return logId;
    }
    public JsonArray getAllScores()
    {
        return ORM.executeQuery(
          "SELECT * FROM project.moderation_action ORDER BY time_of_action DESC"
        );
    }
    public JsonArray getLogsFromAdmin(User admin)
    {
        int adminID = admin.getUid();
        return ORM.executeQuery(
                "SELECT *  FROM project.account a, project.moderation_action s WHERE a.u_id = ? AND s.admin_id = a.u_id GROUP BY a.u_id" ,adminID
        );
    }
    public JsonArray getLogsFromUser(User user)
    {
        int userID = user.getUid();
        return ORM.executeQuery(
                "SELECT *  FROM project.account a, project.moderation_action s WHERE a.u_id = ? AND s.player_id = a.u_id GROUP BY a.u_id" ,userID
        );
    }


}

