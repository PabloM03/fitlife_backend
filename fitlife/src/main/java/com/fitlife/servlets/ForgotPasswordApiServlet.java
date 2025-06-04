package com.fitlife.servlets;

import com.google.gson.Gson;
import com.fitlife.api.ForgotPasswordRequest;
import com.fitlife.api.ForgotPasswordResponse;
import com.fitlife.api.ErrorResponse;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.bd.ConexionBD;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.UUID;
import javax.mail.*;
import javax.mail.internet.*;

@WebServlet(name = "ForgotPasswordAPI", urlPatterns = {"/api/forgot-password"})
public class ForgotPasswordApiServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // 1. Leer JSON de entrada
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        ForgotPasswordRequest r;
        try {
            r = gson.fromJson(sb.toString(), ForgotPasswordRequest.class);
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(gson.toJson(
                new ErrorResponse(false, "JSON mal formado")
            ));
            return;
        }

        // 2. Buscar usuario
        Usuario u = UsuarioDAO.buscarPorEmail(r.email);
        if (u == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().print(gson.toJson(
                new ErrorResponse(false, "Correo no registrado")
            ));
            return;
        }

        // 3. Generar token y guardarlo
        String token = UUID.randomUUID().toString();
        String sql = "INSERT INTO TOKENS_RECUPERACION (usuario_id, token, usado) VALUES (?, ?, FALSE)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, u.getId());
            ps.setString(2, token);
            ps.executeUpdate();
        } catch (SQLException ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(
                new ErrorResponse(false, "Error al generar el token")
            ));
            return;
        }

        // 4. Enviar correo (misma lógica que antes)
        String resetLink = "http://your-domain.com/reset?token=" + token;
        enviarCorreo(u.getEmail(), resetLink);

        // 5. Responder JSON
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(gson.toJson(
            new ForgotPasswordResponse(true,
                "Te hemos enviado un correo con instrucciones")
        ));
    }

    // No permitimos GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        resp.setContentType("application/json");
        resp.getWriter().print(gson.toJson(
            new ErrorResponse(false, "GET no permitido en /api/forgot-password")
        ));
    }

    // Método idéntico al anterior, adaptado:
    private void enviarCorreo(String destino, String enlace) {
        String remitente = "wordpresssalva@gmail.com";
        String clave     = "elpjetlkcfvaljlp";
        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            "smtp.gmail.com");
        props.put("mail.smtp.port",            "587");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(remitente));
            msg.setRecipients(Message.RecipientType.TO,
                              InternetAddress.parse(destino));
            msg.setSubject("Recupera tu contraseña - FitLife");
            msg.setText("Haz clic: " + enlace);
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
