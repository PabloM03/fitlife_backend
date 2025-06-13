// src/main/java/com/fitlife/servlets/UnlikeComidaApiServlet.java
package com.fitlife.servlets;

import com.fitlife.api.GenericResponse;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.LikeDAO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/unlike")
public class UnlikeComidaApiServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        HttpSession session = req.getSession(false);
        Usuario usuario = session != null
            ? (Usuario) session.getAttribute("usuario")
            : null;
        if (usuario == null) {
            sendWrapper(resp, false, "No autorizado");
            return;
        }

        int usuarioId     = usuario.getId();
        int idPublicacion = Integer.parseInt(req.getParameter("id"));

        boolean ok = new LikeDAO().quitarLike(usuarioId, idPublicacion);
        sendWrapper(resp,
            ok,
            ok ? "Like retirado" : "No había like o error"
        );
    }

    private void sendWrapper(HttpServletResponse resp, boolean exito, String mensaje)
            throws IOException {
        Map<String,Object> wrapper = new HashMap<>();
        wrapper.put("exito", exito);
        wrapper.put("mensaje", mensaje);
        String json = new Gson().toJson(wrapper);
        try (PrintWriter out = resp.getWriter()) {
            out.print(json);
        }
    }
}
