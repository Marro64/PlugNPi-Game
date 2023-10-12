package db;

import java.sql.DriverManager;

public enum Connection {
    instance;
    private static java.sql.Connection conn = null;

    public java.sql.Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("org.postgresql.Driver");

                conn = DriverManager.getConnection("jdbc:postgresql://bronto.ewi.utwente.nl/dab_pcsdb23241a_194", "dab_pcsdb23241a_194", "KYz58vHLR2s0UrEq");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}

