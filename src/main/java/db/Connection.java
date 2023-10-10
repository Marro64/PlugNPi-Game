package db;

import java.sql.DriverManager;

public enum Connection {
    instance;
    private static java.sql.Connection conn = null;

    public java.sql.Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("org.postgresql.Driver");

                conn = DriverManager.getConnection("https://bronto.ewi.utwente.nl/phppgadmin/redirect.php?subject=server&server=localhost%3A5432%3Aallow&", "dab_pcsdb23241a_194", "KYz58vHLR2s0UrEq");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}

