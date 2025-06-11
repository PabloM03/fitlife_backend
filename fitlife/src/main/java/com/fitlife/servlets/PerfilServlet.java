// src/main/java/com/fitlife/servlets/PerfilServlet.java
package com.fitlife.servlets;

import com.fitlife.api.ProfileResponse;
import com.fitlife.classes.Usuario;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/perfil")
public class PerfilServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = session != null
                ? (Usuario) session.getAttribute("usuario")
                : null;

        response.setContentType("application/json");

        if (usuario == null) {
            // 401 si no hay sesión válida
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ProfileResponse err = new ProfileResponse(false, "No autorizado");
            response.getWriter().write(gson.toJson(err));
            return;
        }

        // Construimos la respuesta con todos los campos
        ProfileResponse perfil = new ProfileResponse(
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getEdad(),
            usuario.getPeso(),
            usuario.getAltura(),
            usuario.getNivelActividad() != null
                ? usuario.getNivelActividad().name()
                : null,
            usuario.getObjetivo()
        );

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(gson.toJson(perfil));
    }
}
