package model;

public class ConnectionRequest {

    String session;
    boolean connect;

    public ConnectionRequest(String session, boolean connect) {
        this.session = session;
        this.connect = connect;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public boolean isConnect() {
        return connect;
    }

    public void setConnect(boolean connect) {
        this.connect = connect;
    }
}
