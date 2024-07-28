import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.List;
import java.util.Map;


@WebServlet(urlPatterns = {"/dashboard"})
public class Dashboard extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        PrintWriter out = response.getWriter();
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("dashboard.jsp");
        RequestDispatcher errorDispatcher = request.getRequestDispatcher("error.jsp");
        if(session == null) {
            request.setAttribute("message", "Session not found");
            errorDispatcher.forward(request, response);
        }

        try{
            Connection con = Utils.getConnection();
            ResultSet rs = Utils.getUserWithSession(session, con);
            if(rs == null) {
                request.setAttribute("message", "Server error occured");
                errorDispatcher.forward(request, response);
            }
            if(rs.next()){
                request.setAttribute("name", rs.getString("name"));
                request.setAttribute("uid", rs.getString("uid"));
                Map<String, String> games = Utils.getAllGames(con);
                List<String> registered = Utils.getRegisterdGames(con, rs.getString("uid"));
                request.setAttribute("games", games);
                request.setAttribute("registerdGames", registered);
                dispatcher.forward(request, response);
            }else{
                request.setAttribute("message", "Invalid session");
                errorDispatcher.forward(request, response);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
