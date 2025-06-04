package com.fitlife.servlets;

import com.fitlife.classes.Usuario;
import com.fitlife.dao.RutinaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/asignarRutina")
public class AsignarRutinaServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int rutinaId = Integer.parseInt(request.getParameter("rutinaId"));
        boolean asignada = RutinaDAO.asignarRutina(usuario.getId(), rutinaId);

        if (asignada) {
            request.setAttribute("mensaje", "✅ Rutina asignada correctamente.");
        } else {
            request.setAttribute("mensaje", "❌ No se pudo asignar la rutina.");
        }

        // Volver a cargar la lista actualizada de rutinas
        request.setAttribute("rutinas", RutinaDAO.obtenerTodas());
        request.getRequestDispatcher("ver_rutinas.jsp").forward(request, response);
    }
}
