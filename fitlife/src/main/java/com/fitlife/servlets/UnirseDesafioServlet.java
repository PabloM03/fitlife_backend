package com.fitlife.servlets;

import com.fitlife.classes.Usuario;
import com.fitlife.dao.ParticipacionDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/unirseDesafio")
public class UnirseDesafioServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String param = req.getParameter("desafioId");
        if (param == null || param.isEmpty()) {
            // si no viene parámetro, lo redirigimos con un mensaje de error (opcional)
            req.setAttribute("mensaje", "No se indicó a qué desafío unirse.");
            req.getRequestDispatcher("ver_desafios.jsp").forward(req, resp);
            return;
        }

        int desafioId = Integer.parseInt(param);
        ParticipacionDAO.unirse(usuario.getId(), desafioId);
        resp.sendRedirect("misDesafios");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Si alguien entra por GET, redirigimos al listado de desafíos
        resp.sendRedirect("ver_desafios");
    }
}

