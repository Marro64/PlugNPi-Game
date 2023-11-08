package model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

@XmlRootElement
public class LeaderboardScore {

    String username;
    int distance;
    LocalDateTime date_of_record;

    public LeaderboardScore(String username, int distance, LocalDateTime date_of_record) {
        this.username = username;
        this.distance = distance;
        this.date_of_record = date_of_record;
    }

    public LeaderboardScore() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public LocalDateTime getDate_of_record() {
        return date_of_record;
    }

    public void setDate_of_record(LocalDateTime date_of_record) {
        this.date_of_record = date_of_record;
    }



}
