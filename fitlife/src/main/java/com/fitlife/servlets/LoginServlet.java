package com.fitlife.servlets;

import com.google.gson.Gson;
import com.fitlife.api.LoginRequest;
import com.fitlife.api.LoginResponse;
import com.fitlife.api.ErrorResponse;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.utils.SeguridadUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginAPI", urlPatterns = {"/api/login"})
public class LoginServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configurar respuesta
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Leer JSON del cuerpo
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        // Parsear a objeto Java
        LoginRequest loginReq;
        try {
            loginReq = gson.fromJson(sb.toString(), LoginRequest.class);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(new ErrorResponse(false, "JSON mal formado")));
            out.flush();
            return;
        }

        // Lógica de login igual a la tuya
        Usuario usuario = UsuarioDAO.buscarPorEmail(loginReq.email);
        String passwordCliente = loginReq.password;
        String passwordHasheada = SeguridadUtil.hashearPassword(passwordCliente);

        System.out.println("→ Contraseña recibida: " + passwordCliente);
        System.out.println("→ Contraseña hasheada cliente: " + passwordHasheada);
        System.out.println("→ Contraseña hasheada BD: " + (usuario != null ? usuario.getPassword() : "usuario no encontrado"));


        boolean loginCorrecto = usuario != null &&
                usuario.getPassword().equals(SeguridadUtil.hashearPassword(loginReq.password));

        PrintWriter out = response.getWriter();
        if (loginCorrecto) {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);

            LoginResponse loginRes = new LoginResponse(true, usuario.getNombre(), usuario.getId());
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(loginRes));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print(gson.toJson(new ErrorResponse(false, "Correo o contraseña incorrectos.")));
        }

        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(
                gson.toJson(new ErrorResponse(false, "Método no permitido en /api/login")));
    }
}
