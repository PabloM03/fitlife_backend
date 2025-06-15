package com.fitlife.servlets;

import com.google.gson.Gson;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.api.GenericResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;

@WebServlet(name = "EliminarUsuarioApi", urlPatterns = {"/api/eliminar-usuario"})
public class EliminarUsuarioServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(gson.toJson(new GenericResponse(false, "No has iniciado sesión")));
            return;
        }

        // Obtener ID del usuario desde la sesión
        int userId = (int) session.getAttribute("usuario_id");  // Asegúrate de guardar ese atributo al hacer login

        boolean eliminado = UsuarioDAO.eliminarPorId(userId);

        if (eliminado) {
            session.invalidate();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(new GenericResponse(true, "Usuario eliminado correctamente")));
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new GenericResponse(false, "No se pudo eliminar el usuario")));
        }
    }
}
