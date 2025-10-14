import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/transfer")
public class Transfer extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");

        String senderAccount = req.getParameter("senderAccount");
        String recipientAccount = req.getParameter("recipientAccount");
        String amountStr = req.getParameter("amount");

        if (senderAccount == null || recipientAccount == null || amountStr == null ||
                senderAccount.isEmpty() || recipientAccount.isEmpty() || amountStr.isEmpty()) {
            res.getWriter().println("Invalid request: missing parameters.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                res.getWriter().println("Invalid transfer amount.");
                return;
            }
        } catch (NumberFormatException e) {
            res.getWriter().println("Invalid amount format.");
            return;
        }

        Connection con = null;
        try {
            con = ProvideConnection.giveusConnection();
            con.setAutoCommit(false);

            // Debit sender
            String debitQuery = "UPDATE bankaccount SET balance = balance - ? WHERE accountNumber = ? AND balance >= ?";
            PreparedStatement ps = con.prepareStatement(debitQuery);
            ps.setDouble(1, amount);
            ps.setLong(2, Long.parseLong(senderAccount));
            ps.setDouble(3, amount);
            int updated1 = ps.executeUpdate();

            // Credit recipient
            String creditQuery = "UPDATE bankaccount SET balance = balance + ? WHERE accountNumber = ?";
            ps = con.prepareStatement(creditQuery);
            ps.setDouble(1, amount);
            ps.setLong(2, Long.parseLong(recipientAccount));
            int updated2 = ps.executeUpdate();

            if (updated1 > 0 && updated2 > 0) {
                // Record transactions
                RecordTransaction.recordTransactions(con, "TRANSFER_DEBIT", amount, Long.parseLong(senderAccount));
                RecordTransaction.recordTransactions(con, "TRANSFER_CREDIT", amount, Long.parseLong(recipientAccount));

                con.commit();

                RequestDispatcher rd = req.getRequestDispatcher("TransferSuccess.html");
                rd.forward(req, res);
            } else {
                con.rollback();
                res.getWriter().println("Transfer failed: invalid account or insufficient balance.");
            }

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace(res.getWriter());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
