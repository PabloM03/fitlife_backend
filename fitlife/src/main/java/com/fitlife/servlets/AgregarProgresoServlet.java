package com.fitlife.servlets;

import com.google.gson.Gson;
import com.fitlife.api.AgregarProgresoRequest;
import com.fitlife.api.GenericResponse;
import com.fitlife.classes.Progreso;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ProgresoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

@WebServlet(name = "AgregarProgresoAPI", urlPatterns = {"/api/agregarProgreso"})
public class AgregarProgresoServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print(gson.toJson(new GenericResponse(false, "Usuario no autenticado.")));
            out.flush();
            return;
        }

        // Leer y parsear JSON
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                sb.append(linea);
            }
        }

        AgregarProgresoRequest progresoReq;
        try {
            progresoReq = gson.fromJson(sb.toString(), AgregarProgresoRequest.class);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(new GenericResponse(false, "JSON mal formado.")));
            out.flush();
            return;
        }

        try {
            Date fecha = Date.valueOf(progresoReq.fecha);
            Progreso progreso = new Progreso();
            progreso.setUsuarioId(usuario.getId());
            progreso.setFecha(fecha);
            progreso.setPeso(progresoReq.peso);
            progreso.setCalorias(progresoReq.calorias);
            progreso.setObservaciones(progresoReq.observaciones);

            boolean guardado = ProgresoDAO.guardarProgreso(progreso);

            if (guardado) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(new GenericResponse(true, " Progreso registrado correctamente.")));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(new GenericResponse(false, " No se pudo registrar el progreso.")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new GenericResponse(false, " Error al procesar los datos.")));
        }

        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(
                gson.toJson(new GenericResponse(false, "Método no permitido en /api/agregarProgreso")));
    }
}
