import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import java.sql.*;
import jakarta.servlet.http.*;

@WebServlet(urlPatterns = {"/RegisterUser"})
public class RegisterUser extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException{
        String name = request.getParameter("name");
        String branch = request.getParameter("branch");
        String uid = request.getParameter("uid");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");
        if(name == null || branch == null || uid == null || password == null || confirmPassword == null){
            response.sendRedirect("/CMS/register.jsp");
            return; 
        }
        if(!password.equals(confirmPassword)){
            response.sendRedirect("/CMS/register.jsp");
            return; 
        }
        RequestDispatcher errorDispatcher = request.getRequestDispatcher("error.jsp");
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dev", "root", "123456");
            String insertQuery = "INSERT INTO student(name, uid, branch, password) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(insertQuery);
            ps.setString(1, name);
            ps.setString(2, uid.toLowerCase());
            ps.setString(3, branch);
            ps.setString(4, password);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                response.sendRedirect("/CMS/login.jsp");
            } else {
                request.setAttribute("message", "Failed to create account.");
                errorDispatcher.forward(request, response);
            }
            ps.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            
            request.setAttribute("message", "Some error occured");
            errorDispatcher.forward(request, response);
        } 

    }
}