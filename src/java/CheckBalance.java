import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import static java.lang.System.out;

@WebServlet("/checkBalance")
public class CheckBalance extends GenericServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        String password = req.getParameter("password");


        try (Connection con = ProvideConnection.giveusConnection()) {
            String sqry = "select * from bankaccount where password=?";
            PreparedStatement ps = con.prepareStatement(sqry);
            ps.setString(1, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double balfetch = rs.getDouble("balance");
                req.setAttribute("balfetch",balfetch);

                RequestDispatcher rd= req.getRequestDispatcher("displayBalance.jsp");
                rd.forward(req,res);
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    public static class Transaction {
        private long id;
        private String type;
        private double amount;
        private long accountNumber;
        private Timestamp timestamp;

        public Transaction(long id, String type, double amount, long accountNumber, Timestamp timestamp) {
            this.id = id;
            this.type = type;
            this.amount = amount;
            this.accountNumber = accountNumber;
            this.timestamp = timestamp;
        }

        // Getters
        public long getId() { return id; }
        public String getType() { return type; }
        public double getAmount() { return amount; }
        public long getAccountNumber() { return accountNumber; }
        public Timestamp getTimestamp() { return timestamp; }
    }
}

