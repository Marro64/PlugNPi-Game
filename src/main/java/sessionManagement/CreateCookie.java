package sessionManagement;

import jakarta.ws.rs.core.NewCookie;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class CreateCookie {

    public static NewCookie createSession(String sessionId, LocalDateTime expiry) {
        int maxLifetime = (int) Duration.between(LocalDateTime.now(), expiry).toSeconds();
        return new NewCookie("sessionId", sessionId, "/", null, null, maxLifetime, false);
    }

    public static String generateSession() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.now() + " " + LocalTime.now());
    }
}
