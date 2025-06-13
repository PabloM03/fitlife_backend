package com.fitlife.servlets;

import com.fitlife.classes.Usuario;
import com.fitlife.dao.ParticipacionDAO;
import com.fitlife.bd.ConexionBD;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/api/abandonar")
public class AbandonarDesafioApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        HttpSession session = req.getSession(false);
        Usuario usuario = session != null ? (Usuario) session.getAttribute("usuario") : null;
        JsonObject json = new JsonObject();

        if (usuario == null) {
            json.addProperty("exito", false);
            json.addProperty("mensaje", "No autenticado");
            resp.getWriter().print(json.toString());
            return;
        }

        int desafioId;
        try {
            desafioId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            json.addProperty("exito", false);
            json.addProperty("mensaje", "ID inválido");
            resp.getWriter().print(json.toString());
            return;
        }

        boolean ok = ParticipacionDAO.abandonar(usuario.getId(), desafioId);
        json.addProperty("exito", ok);
        json.addProperty("mensaje", ok ? "Desafío abandonado" : "No se pudo abandonar");
        resp.getWriter().print(json.toString());
    }
}
