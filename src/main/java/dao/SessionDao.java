package dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.ORM;
import model.User;
import model.UserSignup;

import java.util.HashMap;
import java.util.HashSet;

public enum SessionDao {
    INSTANCE;

    private final HashMap<String,Integer> sessions = new HashMap<>();
    public int addSession(String session, int u_id) {
        JsonArray addUserQuery =  ORM.executeQuery(
                "INSERT INTO project.session " +
                        "(session_key, u_id) " +
                        "VALUES (?, ?)",
                session, u_id);
        int userId = ((JsonObject) addUserQuery.get(0)).get("u_id").getAsInt();
        return userId;
    }

    public JsonObject getSessiontime(String session) {
        JsonArray userQuery = ORM.executeQuery("SELECT s.expires FROM project.session s WHERE s.session_key = ?",
                session);
        if (userQuery == null || userQuery.size() == 0) return null;
        return (JsonObject) userQuery.get(0);
    }

    public void deleteSession(String session) {
        JsonArray deleteUserQuerry =  ORM.executeQuery(
                "DELETE FROM project.session " +
                        "WHERE session_key = ?",
                session);
    }
    public  void sessionExists(String session){

    }

    public void addPiSession(String pid, int uid) {
        sessions.put(pid,uid);
        System.out.println("SESSIONDAO; " + "PID: " + pid + " UID: " + uid);
    }

    public void resetSession(String pid) {
        sessions.put(pid,-1);
        System.out.println("SESSIONDAO; " + " RESET/PID: " + pid);
    }

    public HashMap<String,Integer> getSessions() {
        return sessions;
    }




}
