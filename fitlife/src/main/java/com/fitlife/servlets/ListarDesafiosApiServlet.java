package com.fitlife.servlets;

import com.fitlife.api.DesafiosResponse;
import com.fitlife.classes.Desafio;
import com.fitlife.dao.DesafioDAO;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/desafios")
public class ListarDesafiosApiServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        List<Desafio> lista = DesafioDAO.listarTodos();

        DesafiosResponse response = new DesafiosResponse(
            true,
            "Lista de desafíos cargada correctamente",
            lista
        );

        resp.getWriter().print(gson.toJson(response));
    }
}
