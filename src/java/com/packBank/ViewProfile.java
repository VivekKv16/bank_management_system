package com.packBank;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class ViewProfile extends HttpServlet {


    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        long accountNumber=Login.getAccount();
        try(Connection con=ProvideConnection.giveusConnection()) {

            String sqry = "SELECT * FROM bankaccount WHERE accountnumber=?";
            PreparedStatement ps = con.prepareStatement(sqry);
            ps.setLong(1, accountNumber);
            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                String name=Signup.getname();
                long accno=Login.getAccount();
                String email=Signup.getemail();

                req.setAttribute("name", name);
                req.setAttribute("accountNumber", accno);
                req.setAttribute("email", email);
            }

            RequestDispatcher rd = req.getRequestDispatcher("viewProfile.jsp");
            rd.forward(req, res);
        }
        catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
