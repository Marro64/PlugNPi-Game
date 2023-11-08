package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.ORM;
import model.ModerationAction;
import model.User;
import model.UserSignup;

public enum ModerationActionDao {
    INSTANCE;

    public void addLog(ModerationAction log)
    {
        JsonArray addLogQuerry = ORM.executeQuery(
                "INSERT INTO project.moderation_action (admin_id, player_id, action_type) VALUES (?, ?, ?::project.action_type)",
                log.getAid(), log.getPid(), log.getActionType().toString()

        );
    }
    public JsonArray getAllLogs()
    {
        return ORM.executeQuery(
          "SELECT a.username AS admin_username, m.action_type, u.username AS user_username, m.time_of_action\n" +
                  "FROM project.account a\n" +
                  "JOIN project.moderation_action m ON a.u_id = m.admin_id\n" +
                  "LEFT JOIN project.account u ON m.player_id = u.u_id\n" +
                  "ORDER by m.time_of_action"
        );
    }
    public JsonArray getLogsFromAdmin(User admin)
    {
        int adminID = admin.getUid();
        return ORM.executeQuery(
                "SELECT a.username AS admin_username, m.action_type, u.username AS user_username, m.time_of_action\n" +
                        "FROM project.account a\n" +
                        "JOIN project.moderation_action m ON a.u_id = m.admin_id\n" +
                        "LEFT JOIN project.account u ON m.player_id = u.u_id\n" +
                        "WHERE a.u_id = ?;" ,adminID
        );
    }
    public JsonArray getLogsFromUser(User user)
    {
        int userID = user.getUid();
        System.out.println("MODDAO: " + userID);
        return ORM.executeQuery(
                "SELECT a.username AS admin_username, m.action_type, u.username AS user_username, m.time_of_action\n" +
                        "FROM project.account a\n" +
                        "JOIN project.moderation_action m ON a.u_id = m.admin_id\n" +
                        "JOIN project.account u ON m.player_id = u.u_id\n" +
                        "WHERE m.player_id = ?;" ,userID
        );
    }

}

