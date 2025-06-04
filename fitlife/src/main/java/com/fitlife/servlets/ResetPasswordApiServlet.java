package com.fitlife.servlets;

import com.google.gson.Gson;
import com.fitlife.api.ResetPasswordRequest;
import com.fitlife.api.ResetPasswordResponse;
import com.fitlife.api.ErrorResponse;
import com.fitlife.bd.ConexionBD;
import com.fitlife.utils.SeguridadUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "ResetPasswordAPI", urlPatterns = {"/api/reset-password"})
public class ResetPasswordApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 1. Leer JSON
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        ResetPasswordRequest r;
        try {
            r = gson.fromJson(sb.toString(), ResetPasswordRequest.class);
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(gson.toJson(
                new ErrorResponse(false, "JSON mal formado")
            ));
            return;
        }

        // 2. Validar token y obtener userId
        int userId;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT usuario_id, usado FROM TOKENS_RECUPERACION WHERE token = ?"
             )) {
            ps.setString(1, r.token);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().print(gson.toJson(
                        new ErrorResponse(false, "Token no válido")
                    ));
                    return;
                }
                if (rs.getBoolean("usado")) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().print(gson.toJson(
                        new ErrorResponse(false, "Token ya usado")
                    ));
                    return;
                }
                userId = rs.getInt("usuario_id");
            }
        } catch (SQLException ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(
                new ErrorResponse(false, "Error al validar token")
            ));
            return;
        }

        // 3. Actualizar contraseña y marcar token usado
        try (Connection conn = ConexionBD.getConnection()) {
            // a) update password
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE USUARIOS SET password = ? WHERE id = ?"
            )) {
                ps.setString(1, SeguridadUtil.hashearPassword(r.newPassword));
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
            // b) mark token as used
            try (PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE TOKENS_RECUPERACION SET usado = TRUE WHERE token = ?"
            )) {
                ps2.setString(1, r.token);
                ps2.executeUpdate();
            }
        } catch (SQLException ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(
                new ErrorResponse(false, "Error al restablecer contraseña")
            ));
            return;
        }

        // 4. Responder éxito
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(gson.toJson(
            new ResetPasswordResponse(true, "Contraseña restablecida")
        ));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        resp.setContentType("application/json");
        resp.getWriter().print(gson.toJson(
            new ErrorResponse(false, "GET no permitido en /api/reset-password")
        ));
    }
}
