package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XmlRootElement
public class UserSignup {
    private String username;
    private String password;

    private String email;

    public UserSignup(){

    }

    public UserSignup(@JsonProperty("username") String username,@JsonProperty("email") String email, @JsonProperty("password") String password){
        setPassword(password);
        this.username = username;
        this.email = email;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = SecurityFactory.createPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}

