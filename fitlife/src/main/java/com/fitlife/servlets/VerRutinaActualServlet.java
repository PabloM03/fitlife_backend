package com.fitlife.servlets;

import com.fitlife.classes.Usuario;
import com.fitlife.classes.Rutina;
import com.fitlife.dao.RutinaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/verRutinaActual")
public class VerRutinaActualServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Rutina rutina = RutinaDAO.obtenerPorUsuarioId(usuario.getId());

        request.setAttribute("rutinaAsignada", rutina);
        request.getRequestDispatcher("rutina_actual.jsp").forward(request, response);
    }
}
