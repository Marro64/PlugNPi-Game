package model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PiScore {

    String pid; //Pi ID
    int distance;

    public PiScore() {
        
    }

    public PiScore(String pid, int distance) {
        this.pid = pid;
        this.distance = distance;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getPid() {
        return pid;
    }

    public int getDistance() {
        return distance;
    }
}
