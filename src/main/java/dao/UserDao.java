package dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.ORM;
import jakarta.ws.rs.core.Response;
import model.User;
import model.UserSignup;

import java.util.Date;

public enum UserDao {
    INSTANCE;

    public boolean UserExists(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        // Build the SQL query to check if a user with the given username and email exists
        String query = "SELECT u_id FROM project.account WHERE username = ? OR email = ?";

        // Execute the query with the provided username and email
        JsonArray userExistsQuery = ORM.executeQuery(query, username, email);
        System.out.println("size of return: " + userExistsQuery.size());

        // Check if the query returned any results
        System.out.println("User exists: " + (userExistsQuery != null && userExistsQuery.size() != 0));

        // Return true if the query found a matching user, otherwise return false
        return userExistsQuery != null && userExistsQuery.size() != 0;
    }


    public JsonObject getByUsername(String username){
        JsonArray userQuery = ORM.executeQuery("SELECT u_id, password FROM project.account WHERE username = ?",
                username);

        if (userQuery == null || userQuery.size() == 0) return null;

        return (JsonObject) userQuery.get(0);
    }

    public int addUser(UserSignup user) {
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
