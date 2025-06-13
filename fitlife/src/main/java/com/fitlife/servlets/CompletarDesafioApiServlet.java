package com.fitlife.servlets;

import com.fitlife.api.GenericResponse;
import com.fitlife.api.MisDesafioResponse;
import com.fitlife.classes.Participacion;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ParticipacionDAO;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/completar")
public class CompletarDesafioApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            resp.getWriter().print(gson.toJson(new GenericResponse(false, "No autenticado")));
            return;
        }

        int id = Integer.parseInt(req.getParameter("id"));
        ParticipacionDAO.marcarCompletado(usuario.getId(), id);

        resp.getWriter().print(gson.toJson(new GenericResponse(true, "Desafío marcado como completado")));
    }
}
