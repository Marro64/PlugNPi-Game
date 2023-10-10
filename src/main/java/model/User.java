package model;
import org.mindrot.jbcrypt.BCrypt;

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
        setPassword(password);
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
        this.password = encryptPassword(password);
    }

    public UserType getUser_type() {
        return usertype;
    }

    public void setUser_type(UserType usertype) {
        this.usertype = usertype;
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
     private String encryptPassword(String plainPassword) {

        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());

    }
}

