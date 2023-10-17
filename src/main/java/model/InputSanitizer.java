package model;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InputSanitizer {

    private static final Set<Character> FORBIDDEN_CHARACTERS = new HashSet<>(Arrays.asList(
            '<', '>', '\'', '"', ';', '-', '{', '}', '[', ']', '=', '*', '&', '|', '$', '%', '!', '`', '~', '?', '/',
            '\\', ':'
    ));

    public static String stringSanitize(String input) {
        if (input == null) {
            return null;
        }

        StringBuilder sanitized = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (!FORBIDDEN_CHARACTERS.contains(c)) {
                sanitized.append(c);
            }
        }
        return sanitized.toString();
    }
    public static User userSanitize(User user) {
        if (user == null) {
            return null;
        }

        user.setEmail(stringSanitize(user.getEmail()));
        user.setUsername(user.getUsername());
        return user;
    }


}
