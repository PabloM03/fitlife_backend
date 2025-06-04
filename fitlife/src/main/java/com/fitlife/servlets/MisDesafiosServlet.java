package com.fitlife.servlets;

import com.fitlife.classes.Participacion;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ParticipacionDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/misDesafios")
public class MisDesafiosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        List<Participacion> participaciones =
            ParticipacionDAO.listarPorUsuario(usuario.getId());
        req.setAttribute("partici", participaciones);
        req.getRequestDispatcher("mis_desafios.jsp").forward(req, resp);
    }
}
