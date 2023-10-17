package dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import db.ORM;
import jakarta.ws.rs.core.Response;
import model.*;

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
        JsonArray userQuery = ORM.executeQuery("SELECT * FROM project.account WHERE username = ?",
                username);

        if (userQuery == null || userQuery.size() == 0) return null;

        return (JsonObject) userQuery.get(0);
    }

    public int addUser(UserSignup user) {
        user = InputSanitizer.signupSanitize(user);
        JsonArray addUserQuerry =  ORM.executeQuery(
                "INSERT INTO project.account " +
                "(username, email, password) " +
                "VALUES (?, ?, ?)",
                user.getUsername(), user.getEmail(), user.getPassword());
        int userId = ((JsonObject) addUserQuerry.get(0)).get("u_id").getAsInt();
        return userId;
    }



    //different method for admin that only they can access.
    public void deleteUser(User user) {
        JsonArray deleteUserQuerry =  ORM.executeQuery(
                "DELETE FROM project.account " +
                "WHERE u_id = ?",
                user.getUid());
    }
    public int updateUser(User user) {
        user = InputSanitizer.userSanitize(user);
        JsonArray updateUserQuerry = ORM.executeQuery(
               "UPDATE project.account SET username = ?, email = ?, password = ?  WHERE u_id =?",
                user.getUsername(), user.getEmail(), user.getPassword(), user.getUid()
        );
        int userId = ((JsonObject) updateUserQuerry.get(0)).get("u_id").getAsInt();
            return userId;


    }
    public void changeUserRole(User user)
    {
        if(user.getUser_type() != null && user.getUser_type() == UserType.PLAYER ) {

            JsonArray updateUserQuerry = ORM.executeQuery(
                    "UPDATE project.account SET u_type = ?::project.u_type WHERE u_id = ? ",
                    "ADMIN", user.getUid());
        } else if (user.getUser_type() != null && user.getUser_type() == UserType.ADMIN) {
            JsonArray updateUserQuerry = ORM.executeQuery(
            "UPDATE project.account SET u_type = ?::project.u_type WHERE u_id = ?",
                    "PLAYER", user.getUid());
        }
    }

    public void jsonToUser(JsonObject jsonObject, User user )
    {
        user.setUsername(jsonObject.get("username").getAsString());
        user.setEmail(jsonObject.get("email").getAsString());
        user.setPassword(jsonObject.get("password").getAsString());
        user.setU_id(jsonObject.get("u_id").getAsInt());
        System.out.println("Checking user type next:");
        user.setUser_type(convertStringToEnum(jsonObject.get("u_type").toString()));
    }

    public UserType convertStringToEnum(String utype) {
        System.out.println(utype);
        if(utype.equals("\"ADMIN\"")) {
            return UserType.ADMIN;
        } else if (utype.equals("\"PLAYER\"")) {
            System.out.println("we have a player");
            return UserType.PLAYER;
        }
        return null;
    }



}



