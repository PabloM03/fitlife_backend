package com.fitlife.servlets;

import com.fitlife.api.RestablecerContrasenaRequest;
import com.fitlife.api.RestablecerContrasenaResponse;
import com.fitlife.bd.ConexionBD;
import com.fitlife.utils.SeguridadUtil;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/api/restablecer")
public class RestablecerContrasenaApiServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        RestablecerContrasenaRequest data = gson.fromJson(req.getReader(), RestablecerContrasenaRequest.class);
        String token = data.getToken();
        String nuevaPassword = data.getPassword();

        if (token == null || token.isEmpty() || nuevaPassword == null || nuevaPassword.isEmpty()) {
            enviarRespuesta(resp, false, "Token o contraseña vacíos.");
            return;
        }

        try (Connection conn = ConexionBD.getConnection()) {

            String select = "SELECT usuario_id, usado FROM TOKENS_RECUPERACION WHERE token = ?";
            try (PreparedStatement stmt = conn.prepareStatement(select)) {
                stmt.setString(1, token);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getBoolean("usado")) {
                            enviarRespuesta(resp, false, "Este token ya fue utilizado.");
                            return;
                        }

                        int usuarioId = rs.getInt("usuario_id");
                        String hashed = SeguridadUtil.hashearPassword(nuevaPassword);

                        // Actualizar la contraseña
                        String updPwd = "UPDATE USUARIOS SET password = ? WHERE id = ?";
                        try (PreparedStatement p2 = conn.prepareStatement(updPwd)) {
                            p2.setString(1, hashed);
                            p2.setInt(2, usuarioId);
                            p2.executeUpdate();
                        }

                        // Marcar token como usado
                        String updTok = "UPDATE TOKENS_RECUPERACION SET usado = TRUE WHERE token = ?";
                        try (PreparedStatement p3 = conn.prepareStatement(updTok)) {
                            p3.setString(1, token);
                            p3.executeUpdate();
                        }

                        enviarRespuesta(resp, true, "✅ Contraseña restablecida con éxito.");
                        return;

                    } else {
                        enviarRespuesta(resp, false, "Token no válido.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            enviarRespuesta(resp, false, "Error interno al restablecer la contraseña.");
        }
    }

    private void enviarRespuesta(HttpServletResponse resp, boolean success, String msg) throws IOException {
        RestablecerContrasenaResponse r = new RestablecerContrasenaResponse(success, msg);
        resp.getWriter().print(gson.toJson(r));
    }
}
