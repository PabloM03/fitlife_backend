// src/main/java/com/fitlife/api/servlets/ListarRutinasApiServlet.java
package com.fitlife.servlets;

import com.fitlife.api.RutinaResponse;
import com.fitlife.classes.Rutina;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.RutinaDAO;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/rutinas")
public class ListarRutinasApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        // Opcional: validar sesión
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            // Devuelve 401 para que el cliente sepa que no está autenticado
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print("{\"success\":false,\"message\":\"No autenticado\"}");
            return;
        }

        // Obtener todas las rutinas
        List<Rutina> all = RutinaDAO.obtenerTodas();
        // Mapear a DTO
        List<RutinaResponse> data = all.stream()
            .map(r -> new RutinaResponse(
                r.getId(),
                r.getNombre(),
                r.getDescripcion(),
                r.getNivel()
            ))
            .collect(Collectors.toList());

        // Serializar directamente la lista
        resp.getWriter().print(gson.toJson(data));
    }
}
