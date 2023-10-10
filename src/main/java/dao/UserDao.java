package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.ORM;
import jakarta.ws.rs.core.Response;

import java.util.Date;

public enum UserDao {
    INSTANCE;

    public boolean UserExists(String username){
        JsonArray userExistsQuery =
                ORM.executeQuery("SELECT u_id FROM project.account WHERE username = ?",
                        username);

        return userExistsQuery != null && userExistsQuery.size() != 0;
    }

    public JsonObject getByUsername(String username){
        JsonArray userQuery = ORM.executeQuery("SELECT u_id, password FROM project.account WHERE username = ?",
                username);

        if (userQuery == null || userQuery.size() == 0) return null;

        return (JsonObject) userQuery.get(0);
    }
}
