// src/main/java/com/fitlife/servlets/UnlikeComidaApiServlet.java
package com.fitlife.servlets;

import com.fitlife.api.GenericResponse;
import com.fitlife.dao.LikeDAO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/unlike")
public class UnlikeComidaApiServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            writeJson(resp, new GenericResponse(false, "No autorizado"));
            return;
        }
        int usuarioId = (Integer) session.getAttribute("usuarioId");
        int idPublicacion = Integer.parseInt(req.getParameter("id"));

        boolean ok = new LikeDAO().quitarLike(usuarioId, idPublicacion);
        GenericResponse resultado = ok
            ? new GenericResponse(true, "Like retirado")
            : new GenericResponse(false, "No había like o error");

        writeJson(resp, resultado);
    }

    private void writeJson(HttpServletResponse resp, GenericResponse response) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.print(new Gson().toJson(response));
        }
    }
}
