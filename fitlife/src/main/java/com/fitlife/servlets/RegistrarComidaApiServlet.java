// src/main/java/com/fitlife/servlets/RegistrarComidaApiServlet.java
package com.fitlife.servlets;

import com.fitlife.api.RegistrarComidaRequest;
import com.fitlife.api.GenericResponse;
import com.fitlife.classes.Comida;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ComidaDAO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet("/api/registrarComida")
public class RegistrarComidaApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        Usuario u = session != null
                  ? (Usuario) session.getAttribute("usuario")
                  : null;
        if (u == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(
                gson.toJson(new GenericResponse(false, "No autorizado"))
            );
            return;
        }

        // Leer body JSON
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = req.getReader()) {
            String line;
            while ((line = r.readLine()) != null) sb.append(line);
        }

        RegistrarComidaRequest dto;
        try {
            dto = gson.fromJson(sb.toString(), RegistrarComidaRequest.class);
        } catch (JsonSyntaxException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(
                gson.toJson(new GenericResponse(false, "JSON inválido"))
            );
            return;
        }

        // Persistir nueva comida con fecha de hoy
        Comida c = new Comida();
        c.setUsuarioId(u.getId());
        c.setFecha(Date.valueOf(LocalDate.now()));
        c.setNombre(dto.nombre);
        c.setCalorias(dto.calorias);
        c.setCarbohidratos(dto.carbohidratos);
        c.setProteinas(dto.proteinas);
        c.setGrasas(dto.grasas);
        c.setObservaciones(dto.observaciones);

        ComidaDAO.guardarComida(c);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(
            gson.toJson(new GenericResponse(true, "Comida registrada correctamente"))
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        resp.setContentType("application/json");
        resp.getWriter().write(
            gson.toJson(new GenericResponse(false,
                "GET no permitido en /api/registrarComida"))
        );
    }
}
