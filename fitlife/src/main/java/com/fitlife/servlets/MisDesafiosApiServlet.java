package com.fitlife.servlets;

import com.fitlife.api.GenericResponse;
import com.fitlife.api.MisDesafioResponse;
import com.fitlife.classes.Usuario;
import com.fitlife.bd.ConexionBD;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        List<MisDesafioResponse> respuesta = new ArrayList<>();
        String sql = "SELECT p.desafio_id, p.completado, d.titulo, d.descripcion, d.fecha_inicio, d.fecha_fin " +
                     "FROM PARTICIPACIONES p " +
                     "JOIN DESAFIOS d ON p.desafio_id = d.id " +
                     "WHERE p.usuario_id=? ORDER BY p.fecha_join";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuario.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MisDesafioResponse r = new MisDesafioResponse();
                    r.setDesafioId(rs.getInt("desafio_id"));
                    r.setCompletado(rs.getBoolean("completado"));
                    r.setTitulo(rs.getString("titulo"));
                    r.setDescripcion(rs.getString("descripcion"));
                    r.setFechaInicio(rs.getDate("fecha_inicio"));
                    r.setFechaFin(rs.getDate("fecha_fin"));
                    respuesta.add(r);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resp.getWriter().print(gson.toJson(new GenericResponse(false, "Error en base de datos")));
            return;
        }

        resp.getWriter().print(gson.toJson(new Object() {
            final boolean success = true;
            final String message = "Desafíos unidos cargados";
            final List<MisDesafioResponse> data = respuesta;
        }));
    }
}

