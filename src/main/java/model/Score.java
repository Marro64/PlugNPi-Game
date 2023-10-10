package model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

@XmlRootElement
public class Score {
    int uid;
    int distance;

    public Score(int uid, int distance) {
        this.uid = uid;
        this.distance = distance;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getUid() {
        return uid;
    }

    public int getDistance() {
        return distance;
    }

}
