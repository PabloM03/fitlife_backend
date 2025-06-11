// src/main/java/com/fitlife/servlets/EditarPerfilServlet.java
package com.fitlife.servlets;

import com.fitlife.api.EditProfileRequest;
import com.fitlife.api.GenericResponse;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.enums.NivelActividad;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/editarPerfil")
public class EditarPerfilServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Redirigimos a /perfil para reutilizar la lógica de GET
        req.getRequestDispatcher("/perfil").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = session != null
                ? (Usuario) session.getAttribute("usuario")
                : null;

        response.setContentType("application/json");
        if (usuario == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            GenericResponse err = new GenericResponse(false, "No autorizado");
            response.getWriter().write(gson.toJson(err));
            return;
        }

        // Leemos el cuerpo JSON
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        EditProfileRequest reqDto;
        try {
            reqDto = gson.fromJson(sb.toString(), EditProfileRequest.class);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            GenericResponse err = new GenericResponse(false, "JSON inválido");
            response.getWriter().write(gson.toJson(err));
            return;
        }

        // Aplicamos sólo los campos no nulos
        try {
            if (reqDto.edad != null) {
                usuario.setEdad(reqDto.edad);
            }
            if (reqDto.peso != null) {
                usuario.setPeso(reqDto.peso);
            }
            if (reqDto.altura != null) {
                usuario.setAltura(reqDto.altura);
            }
            if (reqDto.nivelActividad != null && !reqDto.nivelActividad.isEmpty()) {
                usuario.setNivelActividad(
                    NivelActividad.valueOf(reqDto.nivelActividad)
                );
            }
            if (reqDto.objetivo != null) {
                usuario.setObjetivo(reqDto.objetivo);
            }

            boolean actualizado = UsuarioDAO.actualizarUsuario(usuario);
            if (actualizado) {
                response.setStatus(HttpServletResponse.SC_OK);
                GenericResponse ok = new GenericResponse(true, "Perfil actualizado");
                response.getWriter().write(gson.toJson(ok));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                GenericResponse err = new GenericResponse(false, "No se actualizó usuario");
                response.getWriter().write(gson.toJson(err));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            GenericResponse err = new GenericResponse(false, "Error en el servidor");
            response.getWriter().write(gson.toJson(err));
        }
    }
}
