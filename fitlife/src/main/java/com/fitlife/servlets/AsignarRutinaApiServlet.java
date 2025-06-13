// src/main/java/com/fitlife/api/servlets/AsignarRutinaApiServlet.java s
package com.fitlife.servlets;

import com.fitlife.api.AsignarRutinaResponse;
import com.fitlife.api.RutinaRequest;
import com.fitlife.api.RutinaResponse;
import com.fitlife.classes.Rutina;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.RutinaDAO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/asignarRutina")
public class AsignarRutinaApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        // 1. Validación de sesión
        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
            ? (Usuario) session.getAttribute("usuario")
            : null;
        if (usuario == null) {
            AsignarRutinaResponse unauth =
                new AsignarRutinaResponse(false, "No autenticado", null);
            resp.getWriter().print(gson.toJson(unauth));
            return;
        }

        // 2. Parseo del body JSON
        RutinaRequest dto;
        try (BufferedReader reader = req.getReader()) {
            dto = gson.fromJson(reader, RutinaRequest.class);
        } catch (JsonSyntaxException | IOException e) {
            AsignarRutinaResponse err =
                new AsignarRutinaResponse(false, "Parámetros inválidos", null);
            resp.getWriter().print(gson.toJson(err));
            return;
        }

        // 3. Asignación (UPDATE o INSERT según exista fila)
        int rutinaId = dto.getRutinaId();
        boolean assigned = RutinaDAO.asignarRutina(usuario.getId(), rutinaId);

        // 4. Recuperar todas las rutinas para devolver catálogo actualizado
        List<Rutina> all = RutinaDAO.obtenerTodas();
        List<RutinaResponse> data = all.stream()
            .map(r -> new RutinaResponse(
                r.getId(),
                r.getNombre(),
                r.getDescripcion(),
                r.getNivel()
            ))
            .collect(Collectors.toList());

        // 5. Construir y enviar respuesta
        AsignarRutinaResponse out = new AsignarRutinaResponse(
            assigned,
            assigned
                ? "✅ Rutina asignada correctamente."
                : "❌ No se pudo asignar la rutina.",
            data
        );
        resp.getWriter().print(gson.toJson(out));
    }
}
