package website;

import java.sql.*;
import java.util.*;


public class Database {
    private static Connection connection;

    public boolean connected() {
        return connection != null;
    }

    public boolean usernameTaken(String username) {
        try {
            String query = "select count(1) from user where username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getInt(1) == 1;
        } catch (SQLException ex) {
            System.out.println("SQL usernameTaken() error");
        }
        return true;
    }

    public String getUsername(int userid)
        throws SQLException
    {
        String query = "select username from user where user_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userid);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return result.getString(1);
        }
        throw new SQLException("no username found with such user_id");
    }

    public int getUserId(String username)
        throws SQLException
    {
        String query = "select user_id from user where username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return result.getInt(1);
        }
        throw new SQLException();
    }

    public String getPasswordHash(String username)
            throws SQLException
    {
        String query = "select password_hash from user where username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return result.getString(1);
        }
        throw new SQLException();
    }

    public String getPasswordSalt(String username)
            throws SQLException
    {
        String query = "select password_salt from user where username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return result.getString(1);
        }
        throw new SQLException();
    }

    public List<Integer> getUserSkillIds(String username)
            throws SQLException
    {
        String query = "select skill_id from user_has_skill where username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
        List<Integer> skills = new ArrayList<>();
        while (result.next()) {
            skills.add(result.getInt(1));
        }
        return skills;
    }

    public List<UserSkillUsage> getUserStatistics(String username)
            throws SQLException
    {
        String query = "select count, damage, username, skill_id from user_used_skill where username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
        List<UserSkillUsage> statistics = new ArrayList<>();
        while(result.next()) {
            UserSkillUsage usage = new UserSkillUsage(
                    result.getInt("count"),
                    result.getInt("damage"),
                    result.getString("username"),
                    result.getInt("skill_id"));
            statistics.add(usage);
        }

        return statistics;
    }

    public class UserSkillUsage {
        private int count;
        private int damage;
        private String username;
        private int skillId;

        public int getCount() {
            return count;
        }

        public int getDamage() {
            return damage;
        }

        public String getUsername() {
            return username;
        }

        public int getSkillId() {
            return skillId;
        }

        UserSkillUsage(int count, int damage, String username, int skillId) {
            this.count = count;
            this.damage = damage;
            this.username = username;
            this.skillId = skillId;
        }
    }




    public Map<Integer, String> getSkills()
            throws SQLException
    {
        String query = "select skill_id, skill_name from skill";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();
        Map<Integer, String> skills = new HashMap<>();
        while (result.next()) {
            int skill_id = result.getInt(1);
            String skill_name = result.getString(2);
            skills.put(skill_id, skill_name);
        }
        return skills;
    }

    public void insertUser(String username, String passwordHash, String passwordSalt) throws SQLException {
        // choose new id
        String query = "select max(user_id) from user";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);

        result.next();
        int maxId = result.getInt(1);
        int newId = maxId + 1;

        // insert into db
        String command = "insert into user values (?, ?, ?, ?)";
        PreparedStatement statement1 = connection.prepareStatement(command);
        statement1.setInt(1, newId);
        statement1.setString(2, username);
        statement1.setString(3, passwordHash);
        statement1.setString(4, passwordSalt);
        statement1.executeUpdate();
    }

    public void updateSkills(String username, List<Integer> newSkillIds) throws SQLException {
        List<Integer> skillIds = getUserSkillIds(username);


        PreparedStatement delete = connection.prepareStatement(
                "delete from user_has_skill where skill_id = ?");

        PreparedStatement insert = connection.prepareStatement(
                "insert into user_has_skill values (?, ?)");

        for (int id : skillIds) {
            if (!newSkillIds.contains(id)) {
                delete.setInt(1, id);
                delete.executeUpdate();
            }
        }

        for (int id: newSkillIds) {
            if (!skillIds.contains(id)) {
                insert.setString(1, username);
                insert.setInt(2, id);
                insert.executeUpdate();
            }
        }
    }

    private static Database instance;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            } catch (Exception ex) {
                System.out.println("Database error: couldn't load jdbc driver");
            }
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/WebGame",
                    "root",
                    ""
            );

        } catch (SQLException ex) {
            System.out.println("Couldn't connect to database");
            System.out.println(ex);
        }
    }
}
