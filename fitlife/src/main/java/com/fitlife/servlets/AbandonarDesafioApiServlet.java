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
        JsonObject json = new JsonObject();

        HttpSession session = req.getSession(false);
        Usuario usuario = session != null ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            json.addProperty("exito", false);
            json.addProperty("mensaje", "No autenticado");
            resp.getWriter().print(json.toString());
            return;
        }

        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (Exception e) {
            json.addProperty("exito", false);
            json.addProperty("mensaje", "ID inválido");
            resp.getWriter().print(json.toString());
            return;
        }

        boolean ok = ParticipacionDAO.abandonar(usuario.getId(), id);
        json.addProperty("exito", ok);
        json.addProperty("mensaje", ok
            ? "Desafío abandonado"
            : "No se pudo abandonar");
        resp.getWriter().print(json.toString());
    }
}
