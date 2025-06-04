// src/main/java/com/fitlife/servlets/RegistrarComidaServlet.java
package com.fitlife.servlets;

import com.fitlife.classes.Comida;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ComidaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.sql.Date;

@WebServlet("/registrarComida")
public class RegistrarComidaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = session != null ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lee parámetros del formulario
        String nombre        = request.getParameter("nombre");
        int    calorias      = Integer.parseInt(request.getParameter("calorias"));
        // aquí añadimos los tres nuevos:
        double carbohidratos = request.getParameter("carbohidratos") != null && !request.getParameter("carbohidratos").isEmpty()
                              ? Double.parseDouble(request.getParameter("carbohidratos"))
                              : 0.0;
        double proteinas     = request.getParameter("proteinas")     != null && !request.getParameter("proteinas").isEmpty()
                              ? Double.parseDouble(request.getParameter("proteinas"))
                              : 0.0;
        double grasas        = request.getParameter("grasas")        != null && !request.getParameter("grasas").isEmpty()
                              ? Double.parseDouble(request.getParameter("grasas"))
                              : 0.0;
        String observaciones = request.getParameter("observaciones");

        Comida comida = new Comida();
        comida.setUsuarioId(usuario.getId());
        comida.setFecha(Date.valueOf(LocalDate.now()));
        comida.setNombre(nombre);
        comida.setCalorias(calorias);
        comida.setCarbohidratos(carbohidratos);
        comida.setProteinas(proteinas);
        comida.setGrasas(grasas);
        comida.setObservaciones(observaciones);

        ComidaDAO.guardarComida(comida);
        response.sendRedirect("listarComidas");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("agregar_comida.jsp").forward(request, response);
    }
}
