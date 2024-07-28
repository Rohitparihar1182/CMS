import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/GameServlet")
public class GameServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uid = request.getParameter("uid");
        String[] selectedGames = request.getParameterValues("games");

        try {
            // Establish the database connection
            Connection con = Utils.getConnection();

            // First, clear the existing registrations for the user
            clearUserRegistrations(con, uid);

            // Register the selected games
            if (selectedGames != null) {
                for (String gameId : selectedGames) {
                    registerUserForGame(con, uid, gameId);
                }
            }

            // Redirect back to the JSP page
            response.sendRedirect("/CMS/dashboard");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearUserRegistrations(Connection con, String uid) throws SQLException {
        String deleteQuery = "DELETE FROM student_games WHERE uid = ?";
        PreparedStatement pstmt = con.prepareStatement(deleteQuery);
        pstmt.setString(1, uid);
        pstmt.executeUpdate();
    }

    private void registerUserForGame(Connection con, String uid, String gameId) throws SQLException {
        String insertQuery = "INSERT INTO student_games (uid, game_id) VALUES (?, ?)";
        PreparedStatement pstmt = con.prepareStatement(insertQuery);
        pstmt.setString(1, uid);
        pstmt.setString(2, gameId);
        pstmt.executeUpdate();
    }
}
