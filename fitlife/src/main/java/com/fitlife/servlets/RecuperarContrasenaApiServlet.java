package com.fitlife.servlets;

import com.fitlife.api.RecuperarContrasenaRequest;
import com.fitlife.api.RecuperarContrasenaResponse;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.bd.ConexionBD;
import com.google.gson.Gson;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.UUID;

@WebServlet("/api/recuperar")
public class RecuperarContrasenaApiServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        RecuperarContrasenaRequest recuperarRequest = gson.fromJson(req.getReader(), RecuperarContrasenaRequest.class);
        String email = recuperarRequest.getEmail();

        Usuario usuario = UsuarioDAO.buscarPorEmail(email);
        if (usuario == null) {
            enviarRespuesta(resp, false, "Ese correo no está registrado.");
            return;
        }

        String token = UUID.randomUUID().toString();
        String sql = "INSERT INTO TOKENS_RECUPERACION (usuario_id, token) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuario.getId());
            stmt.setString(2, token);
            stmt.executeUpdate();

            // Aquí debes reemplazar la IP por la pública de tu servidor
            String resetLink = "http://13.61.161.23:8080/fitlife/restablecer?token=" + token;

            boolean enviado = enviarCorreo(email, resetLink);

            if (enviado) {
                enviarRespuesta(resp, true, "Te hemos enviado un correo con las instrucciones.");
            } else {
                enviarRespuesta(resp, false, "No se pudo enviar el correo.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            enviarRespuesta(resp, false, "Error al generar el token.");
        }
    }

    private void enviarRespuesta(HttpServletResponse resp, boolean success, String mensaje) throws IOException {
        RecuperarContrasenaResponse respuesta = new RecuperarContrasenaResponse(success, mensaje);
        resp.getWriter().print(gson.toJson(respuesta));
    }

    private boolean enviarCorreo(String destino, String enlaceRestablecer) {
        String remitente = "wordpresssalva@gmail.com";
        String clave     = "elpjetlkcfvaljlp";  // ⚠️ Debería estar en archivo .env o fuera del código fuente

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session sesion = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
            mensaje.setSubject("Recupera tu contraseña - FitLife");
            mensaje.setText("Haz clic en el siguiente enlace para restablecer tu contraseña:\n\n" + enlaceRestablecer);
            Transport.send(mensaje);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
