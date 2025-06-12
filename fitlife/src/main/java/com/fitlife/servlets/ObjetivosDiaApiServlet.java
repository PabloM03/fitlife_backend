// src/main/java/com/fitlife/api/servlets/ObjetivosDiaApiServlet.java
package com.fitlife.servlets;

import com.fitlife.api.ObjetivosDiaResponse;
import com.fitlife.api.ObjetivoDiaResponse;
import com.fitlife.classes.Rutina;
import com.fitlife.classes.RutinaDia;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.RutinaDAO;
import com.fitlife.dao.RutinaDiaDAO;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@WebServlet("/api/objetivosDia")
public class ObjetivosDiaApiServlet extends HttpServlet {
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
            resp.getWriter().print(
                gson.toJson(new ObjetivosDiaResponse(false, "No autenticado", Collections.emptyList()))
            );
            return;
        }

        Rutina rutina = RutinaDAO.obtenerPorUsuarioId(usuario.getId());
        if (rutina == null) {
            resp.getWriter().print(
                gson.toJson(new ObjetivosDiaResponse(false, "No tienes una rutina asignada.", Collections.emptyList()))
            );
            return;
        }

        DayOfWeek dia = LocalDate.now().getDayOfWeek();
        String nombreDia = switch (dia) {
            case MONDAY    -> "Lunes";
            case TUESDAY   -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY  -> "Jueves";
            case FRIDAY    -> "Viernes";
            case SATURDAY  -> "Sábado";
            case SUNDAY    -> "Domingo";
        };

        RutinaDia diaActual = RutinaDiaDAO.obtenerDiaActual(rutina.getId(), nombreDia);

        List<ObjetivoDiaResponse> data = (diaActual != null)
            ? List.of(new ObjetivoDiaResponse(
                  diaActual.getId(),
                  diaActual.getRutinaId(),
                  diaActual.getDiaSemana(),
                  diaActual.getDescripcion()
              ))
            : Collections.emptyList();

        ObjetivosDiaResponse out = new ObjetivosDiaResponse(
            true,
            diaActual != null
                ? "Objetivo del día obtenido."
                : "No hay ejercicio asignado para hoy.",
            data
        );
        resp.getWriter().print(gson.toJson(out));
    }
}
