import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.security.spec.ECField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.lang.System.out;

@WebServlet("/deposit")
public class deposit extends GenericServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        String password=req.getParameter("password");
        double amount= Double.parseDouble(req.getParameter("amount"));

        try(Connection con=ProvideConnection.giveusConnection()){
            String sqry="select * from bankaccount where password=?";
            PreparedStatement ps=con.prepareStatement(sqry);
            ps.setString(1,password);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                double balfetch = rs.getDouble("balance");
                String iqry="update bankaccount set balance=balance+? where password=?";
                PreparedStatement ps2=con.prepareStatement(iqry);
                ps2.setDouble(1,amount);
                ps2.setString(2,password);
                int update=ps2.executeUpdate();
                if(update>0){

                    long accountNumber = rs.getLong("accountNumber");
                    RecordTransaction.recordTransactions(con, "WITHDRAWAL", amount, accountNumber);
                    RequestDispatcher rd=req.getRequestDispatcher("main.html");
                    rd.forward(req, res);
                }
                else {
                    res.getWriter().println("<h2>failed to deposit<h2>");
                }
            }
        }
        catch(Exception e){
            e.printStackTrace(out);
        }
    }
}
