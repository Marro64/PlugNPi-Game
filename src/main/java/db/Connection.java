package db;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.DriverManager;

public enum Connection {
    instance;
    private static java.sql.Connection conn = null;

    public java.sql.Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("org.postgresql.Driver");
                Dotenv dotenv = Dotenv.load();

                conn = DriverManager.getConnection(dotenv.get("DB_URL"), dotenv.get("DB_USERNAME"), dotenv.get("DB_PASSWORD"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}

