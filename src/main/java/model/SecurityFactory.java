package model;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityFactory {


    public static String createPassword(String plainPassword) {
        String salt = BCrypt.gensalt();
        System.out.println("salt: " + salt);
        return BCrypt.hashpw(plainPassword, salt);
    }

    public static String encryptPassword(String plainPassword, String salt) {
        System.out.println("salt: " + salt);
        return BCrypt.hashpw(plainPassword, salt);
    }

    public static String getSalt(String pw) {
        int dotIndex = pw.indexOf('.');
        if (dotIndex != -1) {
            return pw.substring(0, dotIndex + 1);
        } else {
            return null;
        }
    }
}
