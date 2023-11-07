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
    public JsonObject getUser(int id){
        JsonArray userQuery = ORM.executeQuery("SELECT * FROM project.account WHERE u_id = ?",
                id);

        if (userQuery == null || userQuery.size() == 0) return null;

        return (JsonObject) userQuery.get(0);
    }


    public JsonObject getByUserid(int uid){
        JsonArray userQuery = ORM.executeQuery("SELECT * FROM project.account WHERE u_id = ?",
                uid);

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
                "DELETE FROM project.moderation_action\n" +
                        "WHERE admin_id IN (SELECT u_id FROM project.account WHERE u_id = ?);\n" +
                        "DELETE FROM project.moderation_action\n" +
                        "WHERE player_id IN (SELECT u_id FROM project.account WHERE u_id = ?);\n" +
                        "DELETE FROM project.score\n" +
                        "WHERE u_id IN (SELECT u_id FROM project.account WHERE u_id = ?);\n" +
                        "DELETE FROM project.session\n" +
                        "WHERE u_id IN (SELECT u_id FROM project.account WHERE u_id = ?);\n" +
                        "DELETE FROM project.account WHERE u_id = ?;",
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

    public JsonObject getUserFromSession(String session){
        JsonArray userQuery = ORM.executeQuery("SELECT * FROM project.session s , project.account a WHERE s.session_key = ? AND s.u_id = a.u_id",
                session);
        if (userQuery == null || userQuery.size() == 0) return null;

        return (JsonObject) userQuery.get(0);
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

    public JsonArray getAllUsers(String utype) {
        if(utype.equals("undefined")) {
            return ORM.executeQuery(
                    "SELECT u.username, u.email, u.u_type FROM project.account u");
        } else {
            return ORM.executeQuery(
                    "SELECT u.username, u.email, u.u_type FROM project.account u WHERE u.u_type = ?::project.u_type", utype);
        }
    }
    public boolean isAdmin(User user)
    {
        return (user.getUser_type().equals(UserType.ADMIN));
    }
    public boolean isActive(User user)
    {
        JsonArray userQuery = ORM.executeQuery("SELECT a.active FROM project.account a WHERE a.u_id = ?", user.getUid() );
        String active = userQuery.get(0).getAsString();
        boolean isactive = true;
        if (active.equalsIgnoreCase("false")) {
            isactive = false;

        }
        return  isactive;
    }

    public void de_Activate(User user)
    {
        if( isActive(user) ) {

            ORM.executeQuery("UPDATE project.account SET active = false WHERE a.u_id = ?", user.getUid() );
        } else if (!isActive(user)) {

            ORM.executeQuery("UPDATE project.account SET active = true WHERE a.u_id = ?", user.getUid() );
        }

        }
    }





