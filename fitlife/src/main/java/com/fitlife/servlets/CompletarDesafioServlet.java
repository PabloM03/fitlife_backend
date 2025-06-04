package com.fitlife.servlets;

import com.fitlife.classes.Usuario;
import com.fitlife.dao.ParticipacionDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/completarDesafio")
public class CompletarDesafioServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int desafioId = Integer.parseInt(req.getParameter("id"));
        ParticipacionDAO.marcarCompletado(usuario.getId(), desafioId);
        resp.sendRedirect("misDesafios");
    }
}
