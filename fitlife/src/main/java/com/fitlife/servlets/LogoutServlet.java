package com.fitlife.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // no crear si no existe
        if (session != null) {
            session.invalidate(); // cierra la sesión
        }
        response.sendRedirect("login.jsp");
    }
}
