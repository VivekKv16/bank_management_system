package com.packBank;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.lang.System.out;

@WebServlet("/withdraw")
public class Withdraw extends HttpServlet {


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String password=req.getParameter("password");
        double amount= Double.parseDouble(req.getParameter("amount"));

        try(Connection con=ProvideConnection.giveusConnection()){
            String sqry="select * from bankaccount where password=?";
            PreparedStatement ps=con.prepareStatement(sqry);
            ps.setString(1,password);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                double balfetch = rs.getDouble("balance");
                if(balfetch>=amount){
                    String iqry="update bankaccount set balance=balance-? where password=?";
                    PreparedStatement ps2=con.prepareStatement(iqry);
                    ps2.setDouble(1,amount);
                    ps2.setString(2,password);
                    int updated = ps2.executeUpdate();
                    if(updated>0){
                        long accountNumber = rs.getLong("accountNumber");
                        RecordTransaction.recordTransactions(con, "WITHDRAWAL", amount, accountNumber);
                        RequestDispatcher rd=req.getRequestDispatcher("main.html");
                        rd.forward(req, res);
                    }
                    else{
                        res.getWriter().println("<h3>Failed to withdraw</h3>");
                    }

                }
            }
        }
        catch(Exception e){
            e.printStackTrace(out);
        }

    }
}
