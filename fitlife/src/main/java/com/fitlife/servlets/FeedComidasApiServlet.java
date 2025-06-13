// src/main/java/com/fitlife/servlets/FeedComidasApiServlet.java
package com.fitlife.servlets;

import com.fitlife.api.GenericResponse;
import com.fitlife.api.ComidaFeedResponse;
import com.fitlife.dao.ComidaPublicadaDAO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/feed")
public class FeedComidasApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuarioId") == null) {
            writeJson(resp, new GenericResponse(false, "No autorizado"));
            return;
        }
        int usuarioId = (Integer) session.getAttribute("usuarioId");

        List<ComidaFeedResponse> lista = new ComidaPublicadaDAO().listarFeed(usuarioId);

        Map<String,Object> wrapper = new HashMap<>();
        wrapper.put("exito", true);
        wrapper.put("data", lista);

        writeJson(resp, wrapper);
    }

    private void writeJson(HttpServletResponse resp, Object obj) throws IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.print(new Gson().toJson(obj));
        }
    }
}
