package com.packBank;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/transactions")
public class Transactions extends HttpServlet {

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        long accountNumber = Login.getAccount();
        List<Transaction> transactions = new ArrayList<>();

        try (Connection con = ProvideConnection.giveusConnection()) {
            String sqry = "SELECT * FROM transactions WHERE AccountNumber=? ORDER BY timestamp DESC";
            PreparedStatement ps = con.prepareStatement(sqry);
            ps.setLong(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getLong("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getLong("AccountNumber"),
                        rs.getTimestamp("timestamp")
                ));
            }

            req.setAttribute("transactions", transactions);
            RequestDispatcher rd = req.getRequestDispatcher("transactions.jsp");
            rd.forward(req, res);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            res.getWriter().println("<h3>Error fetching transactions: " + e.getMessage() + "</h3>");
        }
    }
}
