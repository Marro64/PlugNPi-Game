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
    public JsonArray getAllLogs()
    {
        return ORM.executeQuery(
          "SELECT * FROM project.moderation_action ORDER BY time_of_action DESC"
        );
    }
    public JsonArray getLogsFromAdmin(User admin)
    {
        int adminID = admin.getUid();
        return ORM.executeQuery(
                "SELECT a.username m.action_type u.username m.time_of_action FROM project.account a, project.account u, project.moderation_action m WHERE a.u_id = ? AND m.admin_id = a.u_id " ,adminID
        );
    }
    public JsonArray getLogsFromUser(User user)
    {
        int userID = user.getUid();
        return ORM.executeQuery(
                "SELECT a.username m.action_type u.username m.time_of_action FROM project.account a, project.account u, project.moderation_action m WHERE u.u_id = ? AND u.u_id = m.player_id" ,userID
        );
    }


}

