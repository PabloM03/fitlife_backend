package com.fitlife.servlets;

import com.fitlife.api.GenericResponse;
import com.fitlife.api.MisDesafioResponse;
import com.fitlife.api.MisDesafiosListResponse;  
import com.fitlife.classes.Usuario;
import com.fitlife.bd.ConexionBD;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/mis-desafios")
public class MisDesafiosApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        HttpSession session = req.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        if (usuario == null) {
            resp.getWriter().print(gson.toJson(new GenericResponse(false, "No autenticado")));
            return;
        }

        List<MisDesafioResponse> lista = new ArrayList<>();
        String sql = 
          "SELECT p.desafio_id, p.completado, d.titulo, d.descripcion, d.fecha_inicio, d.fecha_fin " +
          "FROM PARTICIPACIONES p " +
          "JOIN DESAFIOS d ON p.desafio_id = d.id " +
          "WHERE p.usuario_id=? ORDER BY p.fecha_join";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuario.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MisDesafioResponse m = new MisDesafioResponse();
                    m.setDesafioId   (rs.getInt   ("desafio_id"));
                    m.setCompletado  (rs.getBoolean("completado"));
                    m.setTitulo      (rs.getString ("titulo"));
                    m.setDescripcion (rs.getString ("descripcion"));
                    m.setFechaInicio (rs.getDate   ("fecha_inicio"));
                    m.setFechaFin    (rs.getDate   ("fecha_fin"));
                    lista.add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resp.getWriter().print(gson.toJson(new GenericResponse(false, "Error en base de datos")));
            return;
        }

        // Usa el DTO concreto en lugar de new Object{…}
        MisDesafiosListResponse out = 
            new MisDesafiosListResponse(true, "Desafíos unidos cargados", lista);
        resp.getWriter().print(gson.toJson(out));
    }
}
