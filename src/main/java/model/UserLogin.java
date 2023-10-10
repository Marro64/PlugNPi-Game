package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserLogin {
    private String username;
    private String passowrd;

    public UserLogin(){

    }

    public UserLogin(@JsonProperty("username") String username, @JsonProperty("password") String passowrd){
        this.passowrd = passowrd;
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }


}
