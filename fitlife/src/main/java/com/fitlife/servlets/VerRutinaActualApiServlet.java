// src/main/java/com/fitlife/api/servlets/VerRutinaActualApiServlet.java
package com.fitlife.servlets;

import com.fitlife.api.VerRutinaActualResponse;
import com.fitlife.api.RutinaResponse;
import com.fitlife.classes.Rutina;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.RutinaDAO;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;



@WebServlet("/api/verRutinaActual")
public class VerRutinaActualApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null)
            ? (Usuario) session.getAttribute("usuario")
            : null;
        if (usuario == null) {
            VerRutinaActualResponse unauth = new VerRutinaActualResponse(
                false,
                "No autenticado",
                Collections.emptyList()
            );
            resp.getWriter().print(gson.toJson(unauth));
            return;
        }

        Rutina current = RutinaDAO.obtenerPorUsuarioId(usuario.getId());
        List<RutinaResponse> data = new ArrayList<>();

        // 1. Declaramos out aquí
        VerRutinaActualResponse out;

        if (current != null) {
            data.add(new RutinaResponse(
                current.getId(),
                current.getNombre(),
                current.getDescripcion(),
                current.getNivel()
            ));
            out = new VerRutinaActualResponse(
                true,
                "Rutina actual obtenida.",
                data
            );
        } else {
            out = new VerRutinaActualResponse(
                true,
                "No hay rutina asignada.",
                Collections.emptyList()
            );
        }

        // 2. Sólo un print al final
        resp.getWriter().print(gson.toJson(out));
    }

}
