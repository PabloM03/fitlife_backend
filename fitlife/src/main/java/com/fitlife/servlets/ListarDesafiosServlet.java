package com.fitlife.servlets;

import com.fitlife.classes.Desafio;
import com.fitlife.dao.DesafioDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/desafios")
public class ListarDesafiosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Desafio> lista = DesafioDAO.listarTodos();
        req.setAttribute("desafios", lista);
        req.getRequestDispatcher("ver_desafios.jsp").forward(req, resp);
    }
}
