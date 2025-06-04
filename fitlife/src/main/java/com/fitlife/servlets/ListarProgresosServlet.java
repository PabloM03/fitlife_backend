package com.fitlife.servlets;

import com.fitlife.classes.Progreso;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ProgresoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;  
import java.sql.Date;        
import java.util.List;

@WebServlet("/listarProgresos")
public class ListarProgresosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario u = (Usuario) req.getSession(false).getAttribute("usuario");
        if (u == null) { resp.sendRedirect("login.jsp"); return; }

        LocalDate hoy      = LocalDate.now();
        LocalDate haceUnaSemana = hoy.minusWeeks(1);

        String sDesde = req.getParameter("desde");
        String sHasta = req.getParameter("hasta");

        LocalDate ldDesde = (sDesde != null && !sDesde.isEmpty())
                            ? LocalDate.parse(sDesde) : haceUnaSemana;
        LocalDate ldHasta = (sHasta != null && !sHasta.isEmpty())
                            ? LocalDate.parse(sHasta) : hoy;

        Date desde = Date.valueOf(ldDesde);
        Date hasta = Date.valueOf(ldHasta);

        List<Progreso> progresos = ProgresoDAO
            .obtenerProgresosPorUsuarioPeriodo(u.getId(), desde, hasta);

        req.setAttribute("progresos", progresos);
        req.setAttribute("desde", ldDesde);
        req.setAttribute("hasta", ldHasta);
        req.getRequestDispatcher("listar_progresos.jsp").forward(req, resp);
    }
}

