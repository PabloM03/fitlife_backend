// RestablecerContrasenaServlet.java
package com.fitlife.servlets;

import com.fitlife.utils.SeguridadUtil;
import com.fitlife.bd.ConexionBD;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/restablecer")
public class RestablecerContrasenaServlet extends HttpServlet {

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String token = request.getParameter("token");
    if (token == null || token.isEmpty()) {
        response.sendRedirect("inicio.jsp");
        return;
    }

    String sql = "SELECT usado FROM TOKENS_RECUPERACION WHERE token = ?";

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        // 1) Ligamos el parámetro ANTES de ejecutar la consulta
        stmt.setString(1, token);

        // 2) Ejecutamos y procesamos el ResultSet
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                boolean usado = rs.getBoolean("usado");
                if (usado) {
                    request.setAttribute("mensaje", "Este enlace ya fue utilizado.");
                    request.getRequestDispatcher("inicio.jsp")
                           .forward(request, response);
                } else {
                    request.setAttribute("token", token);
                    request.getRequestDispatcher("restablecer.jsp")
                           .forward(request, response);
                }
            } else {
                // no hay token en la tabla
                request.setAttribute("mensaje", "Token no válido.");
                request.getRequestDispatcher("inicio.jsp")
                       .forward(request, response);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
        response.sendRedirect("inicio.jsp");
    }
}


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        String nuevaPassword = request.getParameter("password");
        if (token == null || token.isEmpty() ||
            nuevaPassword == null || nuevaPassword.isEmpty()) {
            response.sendRedirect("inicio.jsp");
            return;
        }

        try (Connection conn = ConexionBD.getConnection()) {

            // volvemos a usar la tabla en mayúsculas
            String select = "SELECT usuario_id, usado FROM TOKENS_RECUPERACION WHERE token = ?";
            try (PreparedStatement stmt = conn.prepareStatement(select)) {
                stmt.setString(1, token);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getBoolean("usado")) {
                            request.setAttribute("mensaje", "Este enlace ya fue utilizado.");
                            request.getRequestDispatcher("inicio.jsp").forward(request, response);
                            return;
                        }
                        int usuarioId = rs.getInt("usuario_id");

                        // Hashear nueva pwd
                        String hashed = SeguridadUtil.hashearPassword(nuevaPassword);

                        // Actualiza contraseña
                        String updPwd = "UPDATE USUARIOS SET password = ? WHERE id = ?";
                        try (PreparedStatement p2 = conn.prepareStatement(updPwd)) {
                            p2.setString(1, hashed);
                            p2.setInt(2, usuarioId);
                            p2.executeUpdate();
                        }

                        // Marca token como usado
                        String updTok = "UPDATE TOKENS_RECUPERACION SET usado = TRUE WHERE token = ?";
                        try (PreparedStatement p3 = conn.prepareStatement(updTok)) {
                            p3.setString(1, token);
                            p3.executeUpdate();
                        }

                        request.setAttribute("mensaje", "✅ Contraseña restablecida. Inicia sesión.");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                        return;
                    } else {
                        request.setAttribute("mensaje", "Token no válido.");
                        request.getRequestDispatcher("inicio.jsp").forward(request, response);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Error al restablecer la contraseña.");
            request.getRequestDispatcher("inicio.jsp").forward(request, response);
        }
    }
}
