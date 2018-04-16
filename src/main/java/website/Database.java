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

    public static String getUsername(int userid)
        throws SQLException
    {
        String query = "select username from user where user_id = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setInt(1, userid);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return result.getString(1);
        }
        throw new SQLException();
    }

    public static int getUserId(String username)
        throws SQLException
    {
        String query = "select user_id from user where username = ?";
        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return result.getInt(1);
        }
        throw new SQLException();
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
        int damage = 0;
        int spellsCasted = 0;
        String command = "insert into user values (?, ?, ?, ?, ?)";
        PreparedStatement statement1 = getConnection().prepareStatement(command);
        statement1.setInt(1, newId);
        statement1.setString(2, username);
        statement1.setString(3, passwordHash);
        statement1.setInt(4, damage);
        statement1.setInt(5, spellsCasted);
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
            	try {
            		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            	} catch (Exception ex) {
            		System.out.println("Database error: couldn't load jdbc driver");
            	}
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/WebGame?useSSL=false",
                        "root",
                        "root"
                );

                try {
                    String command = "insert into skill values (0, \"fireball\")";
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(command);
                } catch (SQLException ex) {

                }
            } catch (SQLException ex) {
                System.out.println("Couldn't connect to database");
                System.out.println(ex);
            }
        }

        return connection;
    }

    private Database() {

    }
}
