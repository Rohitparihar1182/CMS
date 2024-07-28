
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dev", "root", "123456");
            return con;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CreateSession createSession(Connection con, String uid, String password, HttpServletRequest request) {
        try {
            String getQuery = "select * from student where uid = ?";
            PreparedStatement ps = con.prepareStatement(getQuery);
            ps.setString(1, uid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean match = rs.getString("password").equals(password);
                if (match) {
                    HttpSession prevSession = request.getSession(false);
                    if (prevSession != null) {
                        prevSession.invalidate();
                        String invalidateSessionQuery = "update student set sessionId = ? where sessionId = ?";
                        PreparedStatement ps2 = con.prepareStatement(invalidateSessionQuery);
                        ps2.setString(1, "");
                        ps2.setString(2, prevSession.getId());
                        ps2.executeUpdate();
                    }

                    HttpSession newSession = request.getSession(true);
                    String newSessionId = newSession.getId();
                    String updateQuery = "UPDATE student SET sessionId = ? WHERE uid = ?";
                    PreparedStatement updateStatement = con.prepareStatement(updateQuery);
                    updateStatement.setString(1, newSessionId);
                    updateStatement.setString(2, uid);
                    int rowsAffected = updateStatement.executeUpdate();

                    // Check if the update was successful
                    if (rowsAffected > 0) {
                        return new CreateSession(newSession, "Session create successfully", true);
                    } else {
                        return new CreateSession(null, "Server Error", false);
                    }

                } else {
                    return new CreateSession(null, "Password did not match", false);
                }
            } else {
                return new CreateSession(null, "Uid not found", false);
            }

        } catch (SQLException e) {
            System.out.println("Some error occured");
            e.printStackTrace();
        }
        return new CreateSession(null, "Some error occured", false);
    }

    public static ResultSet getUserWithSession(HttpSession session, Connection con) {
        try {
            String getQuery = "select * from student where sessionId = ?";
            PreparedStatement ps = con.prepareStatement(getQuery);
            ps.setString(1, session.getId());
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteUserAccount(String uid, Connection con) {
        try {
            String deleteQuery = "DELETE FROM student WHERE uid = ?";
            PreparedStatement ps = con.prepareStatement(deleteQuery);
            ps.setString(1, uid);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean logout(HttpSession session, Connection con) {
        try {
            ResultSet rs = getUserWithSession(session, con);
            if (rs == null) {
                session.invalidate();
                return true;
            }
            if (rs.next()) {
                String uid = rs.getString("uid");
                String updateQuery = "UPDATE student SET sessionId = ? where uid = ?";
                PreparedStatement updateStatement = con.prepareStatement(updateQuery);
                updateStatement.setString(1, null);
                updateStatement.setString(2, uid);
                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    session.invalidate();
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Some error occured");
            e.printStackTrace();
        }
        return false;
    }

    public static Map<String, String> getAllGames(Connection con) {
        Map<String, String> games = new HashMap<>();
        String getQuery = "SELECT * FROM game";

        try (PreparedStatement ps = con.prepareStatement(getQuery); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                games.put(rs.getString("id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    public static List<String> getRegisterdGames(Connection con, String uid) {
        List<String> registeredGames = new ArrayList<>();

        String query = "SELECT game_id FROM student_games WHERE uid = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, uid);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                registeredGames.add(rs.getString("game_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return registeredGames;
    }

    public static boolean registerUserForGame(Connection con, String uid, String gameId) {
        try {
            String insertQuery = "INSERT INTO student_game (uid, game_id) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertQuery);
            pstmt.setString(1, uid);
            pstmt.setString(2, gameId);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean unregisterUserForGame(Connection con, String uid, String gameId) {
        try {
            // Delete the record from student_games table
            String deleteQuery = "DELETE FROM student_game WHERE uid = ? AND game_id = ?";
            PreparedStatement pstmt = con.prepareStatement(deleteQuery);
            pstmt.setString(1, uid);
            pstmt.setString(2, gameId);
            int rowsAffected = pstmt.executeUpdate();

            // Return true if at least one row was affected, false otherwise
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
