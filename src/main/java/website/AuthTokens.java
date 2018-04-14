package website;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AuthTokens {
    private static boolean DEBUGUSER = true;

    private Map<String, String> usernameTokens = new HashMap<>();
    private SecureRandom random = new SecureRandom();

    public String newToken(String username) {
        if (!usernameTokens.containsKey(username)) {
            String token = new BigInteger(130, random).toString(32);
            usernameTokens.put(username, token);
        }
        return usernameTokens.get(username);
    }

    public void removeToken(String username) {
        usernameTokens.remove(username);
    }

    public boolean isValid(String token) {
        // TODO add token expiration
        return usernameTokens.containsValue(token);
    }

    public boolean isValid(int userId, String token) {
        String username = Database.getUsername(userId);
        return usernameTokens.containsValue(token) && usernameTokens.get(username).equals(token);
    }

    public boolean isValid(String username, String password) {
        String password_hash = Database.getUserPasswordHash(username);
        return hash(password).equals(password_hash);
    }

    public String hash(String password) {
        return password;
    }



    private static AuthTokens instance;
    public static AuthTokens getInstance() {
        if (instance == null)
            instance = new AuthTokens();
        return instance;
    }

    private AuthTokens() {
        if (DEBUGUSER) {
            usernameTokens.put("debugname", "debugtoken");
            try {
                Database.insertUser("debugname", "debugpass");
            } catch (SQLException ex) {

            }
        }
    }
}
