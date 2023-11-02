package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.ORM;

import java.util.HashMap;

public enum SessionDao {
    INSTANCE;

    private final HashMap<String,Integer> sessions = new HashMap<>();
    public void addSession(String session, int u_id) {
        JsonArray addSessionQuery =  ORM.executeQuery(
                "INSERT INTO project.session " +
                        "(session_key, u_id) " +
                        "VALUES (?, ?)",
                session, u_id);
    }

    public JsonObject getSessiontime(String session) {
        JsonArray userQuery = ORM.executeQuery("SELECT expires FROM project.session WHERE session_key = ?",
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
        System.out.println("SESSIONDAO; PID: " + pid + " UID: " + uid);
        sessions.put(pid,uid);
    }

    public void resetSession(String pid) {
        System.out.println("SESSIONDAO CLEARING; PID: " + pid);
        sessions.put(pid,-1);
    }

    public HashMap<String,Integer> getSessions() {
        return sessions;
    }




}
