package com.fitlife.servlets;

import com.fitlife.classes.Comida;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ComidaDAO;
import com.fitlife.utils.NutricionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

@WebServlet("/listarComidas")
public class ListarComidasServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = session != null ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Date hoy = Date.valueOf(LocalDate.now());
        List<Comida> comidas = ComidaDAO.obtenerPorUsuarioYFecha(usuario.getId(), hoy);

        int total = comidas.stream()
                           .mapToInt(Comida::getCalorias)
                           .sum();
        int rec = NutricionUtil.getCaloriasRecomendadas(usuario);

        request.setAttribute("comidas", comidas);
        request.setAttribute("totalCalorias", total);
        request.setAttribute("caloriasRec", rec);
        request.getRequestDispatcher("listar_comida.jsp").forward(request, response);
    }
}
