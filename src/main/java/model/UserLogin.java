package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserLogin {
    private String username;
    private String password;

    public UserLogin(){

    }

    public UserLogin(@JsonProperty("username") String username, @JsonProperty("password") String password){
        this.password = password;
        this.username = username;
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
        this.password = password;
    }


}
