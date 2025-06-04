package com.fitlife.servlets;

import com.fitlife.classes.Rutina;
import com.fitlife.classes.RutinaCompletada;
import com.fitlife.classes.RutinaDia;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.RutinaCompletadaDAO;
import com.fitlife.dao.RutinaDAO;
import com.fitlife.dao.RutinaDiaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;

@WebServlet("/completarRutina")
public class CompletarRutinaServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Rutina rutina = RutinaDAO.obtenerPorUsuarioId(usuario.getId());

        if (rutina == null) {
            request.setAttribute("mensaje", "No tienes ninguna rutina asignada.");
            request.getRequestDispatcher("objetivos_dia.jsp").forward(request, response);
            return;
        }

        DayOfWeek dia = LocalDate.now().getDayOfWeek();
        String nombreDia = switch (dia) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };

        // Verificar si ya está registrado como completado hoy
        boolean yaCompletado = RutinaCompletadaDAO.yaCompletadoHoy(usuario.getId(), rutina.getId(), nombreDia);

        if (yaCompletado) {
            request.setAttribute("mensaje", "⚠️ Ya has marcado la rutina de hoy como completada.");
        } else {
            RutinaDia diaActual = RutinaDiaDAO.obtenerDiaActual(rutina.getId(), nombreDia);
            String descripcion = (diaActual != null) ? diaActual.getDescripcion() : "Descanso";

            RutinaCompletada rutinaCompletada = new RutinaCompletada();
            rutinaCompletada.setUsuarioId(usuario.getId());
            rutinaCompletada.setRutinaId(rutina.getId());
            rutinaCompletada.setFecha(Date.valueOf(LocalDate.now()));
            rutinaCompletada.setDiaSemana(nombreDia);
            rutinaCompletada.setDescripcion(descripcion);
            rutinaCompletada.setCompletado(true);

            boolean registrado = RutinaCompletadaDAO.registrarCompletado(rutinaCompletada);

            if (registrado) {
                request.setAttribute("mensaje", "✅ ¡Has completado tu rutina de hoy con éxito!");
            } else {
                request.setAttribute("mensaje", "❌ Error al registrar la rutina completada.");
            }
        }

        // Vuelve a cargar la página de objetivos del día
        response.sendRedirect("objetivosDia");

    }
}
