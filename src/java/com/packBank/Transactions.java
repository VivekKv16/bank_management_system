package com.packBank;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/transaction")
public class Transactions extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        long accountNumber = Long.parseLong(req.getParameter("accountNumber"));
        List<CheckBalance.Transaction> transactions = new ArrayList<>();

        try (Connection con = ProvideConnection.giveusConnection()) {
            String sqry = "select * from transactions where AccountNumber=? order by timestamp desc";
            PreparedStatement ps = con.prepareStatement(sqry);
            ps.setLong(1, accountNumber);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CheckBalance.Transaction t = new CheckBalance.Transaction(
                        rs.getLong("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getLong("AccountNumber"),
                        rs.getTimestamp("timestamp")
                );
                transactions.add(t);
            }

            req.setAttribute("transactions", transactions);
            RequestDispatcher rd = req.getRequestDispatcher("transactions.jsp");
            rd.forward(req, res);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
