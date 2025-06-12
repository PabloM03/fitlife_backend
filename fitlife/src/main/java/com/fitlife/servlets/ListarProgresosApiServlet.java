package com.fitlife.servlets;

import com.fitlife.api.ListarProgresosRequest;
import com.fitlife.api.ListarProgresosResponse;
import com.fitlife.api.ProgresoResponse;
import com.fitlife.classes.Progreso;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.ProgresoDAO;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/listarProgresos")
public class ListarProgresosApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Validar sesión
        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        ListarProgresosResponse responseDto = new ListarProgresosResponse();

        if (u == null) {
            responseDto.success = false;
            responseDto.message = "No autenticado";
            out.print(gson.toJson(responseDto));
            return;
        }

        try {
            LocalDate hoy = LocalDate.now();
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

            List<ProgresoResponse> dtoList = progresos.stream().map(p -> {
                ProgresoResponse pr = new ProgresoResponse();
                pr.id            = p.getId();
                pr.fecha         = p.getFecha().toString();
                pr.peso          = p.getPeso();
                pr.calorias      = p.getCalorias();        
                pr.observaciones = p.getObservaciones();   
                return pr;
            }).collect(Collectors.toList());


            responseDto.success   = true;
            responseDto.message   = "OK";
            responseDto.progresos = dtoList;

        } catch (Exception e) {
            responseDto.success = false;
            responseDto.message = "Error al listar progresos: " + e.getMessage();
        }

        out.print(gson.toJson(responseDto));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Si prefieres usar POST con body JSON para filtros:
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        ListarProgresosResponse responseDto = new ListarProgresosResponse();

        HttpSession session = req.getSession(false);
        Usuario u = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (u == null) {
            responseDto.success = false;
            responseDto.message = "No autenticado";
            out.print(gson.toJson(responseDto));
            return;
        }

        try (BufferedReader reader = req.getReader()) {
            ListarProgresosRequest rq = gson.fromJson(reader, ListarProgresosRequest.class);

            LocalDate hoy = LocalDate.now();
            LocalDate haceUnaSemana = hoy.minusWeeks(1);

            LocalDate ldDesde = (rq.desde != null && !rq.desde.isEmpty())
                    ? LocalDate.parse(rq.desde) : haceUnaSemana;
            LocalDate ldHasta = (rq.hasta != null && !rq.hasta.isEmpty())
                    ? LocalDate.parse(rq.hasta) : hoy;

            Date desde = Date.valueOf(ldDesde);
            Date hasta = Date.valueOf(ldHasta);

            List<Progreso> progresos = ProgresoDAO
                .obtenerProgresosPorUsuarioPeriodo(u.getId(), desde, hasta);

            List<ProgresoResponse> dtoList = progresos.stream().map(p -> {
                ProgresoResponse pr = new ProgresoResponse();
                pr.id            = p.getId();
                pr.fecha         = p.getFecha().toString();
                pr.peso          = p.getPeso();
                pr.calorias      = p.getCalorias();        
                pr.observaciones = p.getObservaciones();  
                return pr;
            }).collect(Collectors.toList());


            responseDto.success   = true;
            responseDto.message   = "OK";
            responseDto.progresos = dtoList;

        } catch (JsonSyntaxException jse) {
            responseDto.success = false;
            responseDto.message = "JSON inválido: " + jse.getMessage();
        } catch (Exception e) {
            responseDto.success = false;
            responseDto.message = "Error interno: " + e.getMessage();
        }

        out.print(gson.toJson(responseDto));
    }
}
