// RecuperarContrasenaServlet.java
package com.fitlife.servlets;

import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.bd.ConexionBD;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.UUID;

@WebServlet("/recuperar")
public class RecuperarContrasenaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("recuperar.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        Usuario usuario = UsuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            request.setAttribute("mensaje", "Ese correo no está registrado.");
            request.getRequestDispatcher("recuperar.jsp").forward(request, response);
            return;
        }

        String token = UUID.randomUUID().toString();

        // Inserta el token en MySQL usando ConexionBD
        String sql = "INSERT INTO TOKENS_RECUPERACION (usuario_id, token) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuario.getId());
            stmt.setString(2, token);
            stmt.executeUpdate();

            // ENLACE ESTÁTICO (no tocamos el punto 5)
            String resetLink = "http://localhost:8080/fitlife-1.0-SNAPSHOT/restablecer?token=" + token;
            enviarCorreo(email, resetLink);

            request.setAttribute("mensaje", "Te hemos enviado un correo con las instrucciones.");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Hubo un problema al generar el enlace.");
        }

        request.getRequestDispatcher("recuperar.jsp").forward(request, response);
    }

    private void enviarCorreo(String destino, String enlaceRestablecer) {
        String remitente = "wordpresssalva@gmail.com";
        String clave     = "elpjetlkcfvaljlp";

        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");

        Session sesion = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destino)
            );
            mensaje.setSubject("Recupera tu contraseña - FitLife");
            mensaje.setText(
                "Haz clic en el siguiente enlace para restablecer tu contraseña:\n\n" +
                enlaceRestablecer
            );

            Transport.send(mensaje);
            System.out.println("[EMAIL] Correo enviado a " + destino);
        } catch (MessagingException e) {
            e.printStackTrace(); // punto 4 sin modificar
        }
    }
}
