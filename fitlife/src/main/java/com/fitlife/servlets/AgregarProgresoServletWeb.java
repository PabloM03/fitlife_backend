package com.fitlife.servlets;

import com.fitlife.classes.Progreso;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ProgresoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/agregarProgreso")
public class AgregarProgresoServletWeb extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            Date fecha = Date.valueOf(request.getParameter("fecha"));
            double peso = Double.parseDouble(request.getParameter("peso"));
            int calorias = Integer.parseInt(request.getParameter("calorias"));
            String observaciones = request.getParameter("observaciones");

            Progreso progreso = new Progreso();
            progreso.setUsuarioId(usuario.getId());
            progreso.setFecha(fecha);
            progreso.setPeso(peso);
            progreso.setCalorias(calorias);
            progreso.setObservaciones(observaciones);

            boolean guardado = ProgresoDAO.guardarProgreso(progreso);

            if (guardado) {
                request.setAttribute("mensaje", " Progreso registrado correctamente.");
            } else {
                request.setAttribute("mensaje", " No se pudo registrar el progreso.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", " Error al procesar los datos.");
        }

        request.getRequestDispatcher("agregar_progreso.jsp").forward(request, response);
    }
}
