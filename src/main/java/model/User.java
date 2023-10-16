package model;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XmlRootElement
public class User {
    private int uid;
    private String username;
    private String email;
    private String password;
    private UserType usertype;

    public User(){

    }

    public User(int uid, String username, String email, String password, UserType usertype) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.usertype = usertype;
    }

    public int getUid() {
        return uid;
    }

    public void setU_id(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUser_type() {
        return usertype;
    }

    public void setUser_type(UserType usertype) {
        this.usertype = usertype;
    }

    public String getSalt() {
        int dotIndex = password.indexOf('.');
        if (dotIndex != -1) {
            return password.substring(0, dotIndex + 1);
        } else {
            return null;
        }
    }


    @Override
    public String toString() {
        return "User{" +
                "u_id=" + uid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", user_type=" + usertype +
                '}';
    }

}

