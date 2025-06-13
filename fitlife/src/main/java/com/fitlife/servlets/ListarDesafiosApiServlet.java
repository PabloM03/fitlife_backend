package com.fitlife.servlets;

import com.fitlife.api.DesafioResponse;
import com.fitlife.api.DesafiosResponse;
import com.fitlife.api.GenericResponse;
import com.fitlife.classes.Desafio;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.DesafioDAO;
import com.fitlife.dao.ParticipacionDAO;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/desafios")
public class ListarDesafiosApiServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            GenericResponse error = new GenericResponse(false, "No estás autenticado.");
            resp.getWriter().print(gson.toJson(error));
            return;
        }

        List<Desafio> desafios = DesafioDAO.listarTodos();
        List<DesafioResponse> respuesta = new ArrayList<>();

        for (Desafio d : desafios) {
            DesafioResponse dr = new DesafioResponse();
            dr.setId(d.getId());
            dr.setTitulo(d.getTitulo());
            dr.setDescripcion(d.getDescripcion());
            dr.setFechaInicio(d.getFechaInicio());
            dr.setFechaFin(d.getFechaFin());

            // Aquí consultamos si ya está unido
            boolean unido = ParticipacionDAO.estaUnido(usuario.getId(), d.getId());
            dr.setYaUnido(unido);

            respuesta.add(dr);
        }

        DesafiosResponse response = new DesafiosResponse(true, "Lista de desafíos", respuesta);
        resp.getWriter().print(gson.toJson(response));
    }
}
