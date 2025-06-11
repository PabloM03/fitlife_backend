package com.fitlife.servlets;

import com.fitlife.classes.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/perfil")
public class PerfilWebServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) (session != null ? session.getAttribute("usuario") : null);

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.setAttribute("usuario", usuario);
        request.getRequestDispatcher("perfil.jsp").forward(request, response);
    }
}
