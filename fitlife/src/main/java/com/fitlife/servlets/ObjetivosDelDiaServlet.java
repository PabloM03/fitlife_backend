package com.fitlife.servlets;

import com.fitlife.classes.Rutina;
import com.fitlife.classes.RutinaDia;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.RutinaDAO;
import com.fitlife.dao.RutinaDiaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;

@WebServlet("/objetivosDia")
public class ObjetivosDelDiaServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Rutina rutina = RutinaDAO.obtenerPorUsuarioId(usuario.getId());

        if (rutina == null) {
            request.setAttribute("mensaje", "No tienes una rutina asignada.");
            request.getRequestDispatcher("objetivos_dia.jsp").forward(request, response);
            return;
        }

        // Día de la semana actual en español (adaptamos el formato)
        DayOfWeek dia = LocalDate.now().getDayOfWeek(); // MONDAY, TUESDAY, ...
        String nombreDia = switch (dia) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };

        RutinaDia diaActual = RutinaDiaDAO.obtenerDiaActual(rutina.getId(), nombreDia);

        request.setAttribute("nombreDia", nombreDia);
        request.setAttribute("rutina", rutina);
        request.setAttribute("ejercicioDelDia", diaActual);
        request.getRequestDispatcher("objetivos_dia.jsp").forward(request, response);
    }
}
