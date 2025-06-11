// src/main/java/com/fitlife/servlets/ListarComidasApiServlet.java
package com.fitlife.servlets;

import com.fitlife.api.ComidaResponse;
import com.fitlife.api.ListarComidasResponse;
import com.fitlife.api.GenericResponse;
import com.fitlife.classes.Comida;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ComidaDAO;
import com.fitlife.utils.NutricionUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/listarComidas")
public class ListarComidasApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(false);
        Usuario u = session != null
                  ? (Usuario) session.getAttribute("usuario")
                  : null;
        if (u == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(
                gson.toJson(new ListarComidasResponse("No autorizado"))
            );
            return;
        }

        Date hoy = Date.valueOf(LocalDate.now());
        List<Comida> comidas = ComidaDAO.obtenerPorUsuarioYFecha(u.getId(), hoy);
        int total = comidas.stream().mapToInt(Comida::getCalorias).sum();
        int rec = NutricionUtil.getCaloriasRecomendadas(u);

        List<ComidaResponse> lista = comidas.stream()
            .map(c -> new ComidaResponse(
                c.getNombre(),
                c.getCalorias(),
                c.getCarbohidratos(),
                c.getProteinas(),
                c.getGrasas(),
                c.getObservaciones(),
                c.getFecha().toString()
            ))
            .collect(Collectors.toList());

        ListarComidasResponse out = new ListarComidasResponse(lista, total, rec);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(gson.toJson(out));
    }
}
