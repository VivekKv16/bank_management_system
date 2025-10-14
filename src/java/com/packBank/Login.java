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
@WebServlet("/login")
public class Login extends HttpServlet {

    static Connection con=null;
    static PreparedStatement ps=null;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email=req.getParameter("email");
        String password=req.getParameter("password");

        try(Connection con=ProvideConnection.giveusConnection()){
        String sqry="select * from bankaccount where email=? and password=?";
        ps=con.prepareStatement(sqry);
        ps.setString(1,email);
        ps.setString(2,password);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                RequestDispatcher rd = req.getRequestDispatcher("main.html");
                rd.forward(req, res);
            }
            else{
                res.getWriter().println("<h3>Failed to Login</h3>");
            }

        }

        catch(Exception e){
            e.printStackTrace(out);
        }
    }
}
