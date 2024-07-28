import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.*;

/**
 *
 * @author rohit
 */
@WebServlet(urlPatterns = {"/DeleteAccount"})
public class DeleteAccount extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String password = request.getParameter("password");
        Connection con = Utils.getConnection();
        RequestDispatcher successDispatcher = request.getRequestDispatcher("success.jsp");
        RequestDispatcher errorDispatcher = request.getRequestDispatcher("error.jsp");
        if(session == null) {
            request.setAttribute("message", "Session not found");
            errorDispatcher.forward(request, response);
        }
        if(password == null) {
            request.setAttribute("message", "Password not found");
            errorDispatcher.forward(request, response);
        }
        PrintWriter out = response.getWriter();
        
        try{
            ResultSet rs = Utils.getUserWithSession(session, con);
            if(rs == null){
                request.setAttribute("message", "Session not found");
                errorDispatcher.forward(request, response);
            }
            if(rs.next()){
                String match = rs.getString("password");
                if(match.equals(password)){
                    boolean deleted = Utils.deleteUserAccount(rs.getString("uid"), con);
                    if(deleted){
                        request.setAttribute("message", "Account Deleted Successfully");
                        successDispatcher.forward(request, response);
                    }else{
                        request.setAttribute("message", "500 Server error");
                        errorDispatcher.forward(request, response);
                    }
                }else{
                    request.setAttribute("message", "Password did not match");
                    errorDispatcher.forward(request, response);
                }
            }else{
                request.setAttribute("message", "500 Server error");
                errorDispatcher.forward(request, response);
            }
        }catch(SQLException e){
            e.printStackTrace();
            request.setAttribute("message", "500 Server error");
            errorDispatcher.forward(request, response);
        }
    }
}
