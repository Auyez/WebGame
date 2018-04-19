package website;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AuthTokens {
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

    public boolean isValid(String username, String token) {
        return usernameTokens.containsKey(username) && usernameTokens.get(username).equals(token);
    }

    public boolean isValid(int userId, String token) {
        try {
            String username = Database.getInstance().getUsername(userId);
            return isValid(username, token);
        } catch (SQLException ex) {
            return false;
        }
    }

    private static AuthTokens instance;
    public static AuthTokens getInstance() {
        if (instance == null)
            instance = new AuthTokens();
        return instance;
    }

    private AuthTokens() {
    }
}
