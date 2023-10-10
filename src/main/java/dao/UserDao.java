package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.ORM;
import jakarta.ws.rs.core.Response;
import model.User;

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
    public int addUser(User user) {
        JsonArray addUserQuerry =  ORM.executeQuery(
                "INSERT INTO project.account " +
                "(username, email, password) " +
                "VALUES (?, ?, ?)",
                user.getUsername(), user.getEmail(), user.getPassword());
        int userId = ((JsonObject) addUserQuerry.get(0)).get("u_id").getAsInt();
        return userId;
    }



//TODO find out how this shit works with deletion and storing
    //different method for admin that only they can access.
    public void deleteUser(User user) {
        JsonArray deleteUserQuerry =  ORM.executeQuery(
                "CASCADE DELETE FROM project.account " +
                "WHERE u_id = ?",
                user.getUid());
    }
    public int updateUser(User user) {
        JsonArray updateUserQuerry = ORM.executeQuery(
               "UPDATE project.account SET username = ?, email = ?, password = ? ",
                user.getUsername(), user.getEmail(), user.getPassword()
        );
        int userId = ((JsonObject) updateUserQuerry.get(0)).get("u_id").getAsInt();
            return userId;


    }


}
