package com.fitlife.servlets;

import com.fitlife.api.GenericResponse;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ParticipacionDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/unirse")
public class UnirseDesafioApiServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            GenericResponse r = new GenericResponse(false, "No estás autenticado.");
            resp.getWriter().print(gson.toJson(r));
            return;
        }

        BufferedReader reader = req.getReader();
        Map<String, Double> map = gson.fromJson(reader, Map.class);

        if (!map.containsKey("desafioId")) {
            GenericResponse r = new GenericResponse(false, "Falta el parámetro 'desafioId'.");
            resp.getWriter().print(gson.toJson(r));
            return;
        }

        int desafioId = map.get("desafioId").intValue(); // Gson lo convierte a Double
        ParticipacionDAO.unirse(usuario.getId(), desafioId);

        JsonObject response = new JsonObject();
        response.addProperty("exito", true);
        response.addProperty("mensaje", "¡Te has unido al desafío!");
        resp.getWriter().print(response.toString());

    }
}
