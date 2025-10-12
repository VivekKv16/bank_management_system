
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static java.lang.System.out;


@WebServlet("/signup")
public class Signup extends GenericServlet {

    static PreparedStatement ps=null;
    static Connection con=null;
    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        String name=req.getParameter("name");
        String email=req.getParameter("email");
        String password=req.getParameter("password");
        double balance= Double.parseDouble(req.getParameter("balance"));

        try (Connection con = ProvideConnection.giveusConnection()) {
            String iqry = "INSERT INTO bankaccount(name,email,password,balance) values(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(iqry);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setDouble(4, balance);

            int res = ps.executeUpdate();
            if (res > 0) {
                RequestDispatcher rd = req.getRequestDispatcher("login.html");
                rd.forward(req, resp);
            }
            else{
                resp.getWriter().println("<h3>Failed to create account</h3>");
            }

        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }


}

