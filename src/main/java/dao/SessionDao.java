package dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.ORM;
import model.User;
import model.UserSignup;

public enum SessionDao {
    INSTANCE;

    public static void deleteSession(String session) {
        //TODO check if session is unique, otherwise also add u_id as param

    }

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
        JsonArray userQuery = ORM.executeQuery("SELECT expires FROM project.session WHERE session_key = ?",
                session);
        if (userQuery == null || userQuery.size() == 0) return null;

        return (JsonObject) userQuery.get(0);
    }

    public void jsonToUser(JsonObject jsonObject, User user )
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            user = objectMapper.readValue(jsonObject.toString(), User.class);
        } catch (Exception e) {
        }
    }
}
