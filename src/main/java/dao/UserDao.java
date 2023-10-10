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
                ORM.executeQuery("SELECT id FROM public.user WHERE phone = ?",
                        username);

        return userExistsQuery != null && userExistsQuery.size() != 0;
    }

    public JsonObject getByUsername(String username){
        JsonArray userQuery = ORM.executeQuery("SELECT uid, password FROM public.user WHERE username = ?",
                username);

        if (userQuery == null || userQuery.size() == 0) return null;

        return (JsonObject) userQuery.get(0);
    }
}
