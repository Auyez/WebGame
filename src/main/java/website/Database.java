package website;

import java.sql.*;
import java.util.Properties;


public class Database {
    private static Connection connection;

    public static boolean connected() {
        return getConnection() != null;
    }

    public static boolean usernameTaken(String username) {
        try {
            String query = "select count(1) from user where username = ?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getInt(1) == 1;
        } catch (SQLException ex) {
            System.out.println("SQL usernameTaken() error");
        }
        return true;
    }

    public static String getUserPasswordHash(String username) {
        try {
            String query = "select password_hash from user where username = ?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getString(1);
            }
        } catch (SQLException ex) {
            //System.out.println("SQL getUserPasswordHash() error");
        }
        return null;
    }

    public static String getUsername(int userid) {
        try {
            String query = "select username from user where user_id = ?";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, userid);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getString(1);
            }
        } catch (SQLException ex) {
            //System.out.println("SQL getUsername() by id error");
        }
        return null;
    }

    public static void insertUser(String username, String passwordHash) throws SQLException {
        // choose new id
        String query = "select max(user_id) from user";
        Statement statement = getConnection().createStatement();
        ResultSet result = statement.executeQuery(query);

        result.next();
        int maxId = result.getInt(1);
        int newId = maxId + 1;

        // insert into db
        int rating = 0;
        String command = "insert into user values (?, ?, ?, ?)";
        PreparedStatement statement1 = getConnection().prepareStatement(command);
        statement1.setInt(1, newId);
        statement1.setString(2, username);
        statement1.setString(3, passwordHash);
        statement1.setInt(4, rating);
        statement1.executeUpdate();

        // default skill for the user
        command = "insert into user_has_skill values (?, 0)";
        statement1 = getConnection().prepareStatement(command);
        statement1.setString(1, username);
        statement1.executeUpdate();
    }

    private static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/WebGame",
                        "root",
                        ""
                );
            } catch (SQLException ex) {
                System.out.println("Couldn't connect to database");
            }
        }

        return connection;
    }

    private Database() {

    }
}
