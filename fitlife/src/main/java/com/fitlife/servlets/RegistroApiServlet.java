package com.fitlife.servlets;

import com.google.gson.Gson;
import com.fitlife.api.RegisterRequest;
import com.fitlife.api.ErrorResponse;
import com.fitlife.api.SuccessResponse;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.utils.SeguridadUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegistroAPI", urlPatterns = {"/api/register"})
public class RegistroApiServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        RegisterRequest req;
        try {
            req = gson.fromJson(sb.toString(), RegisterRequest.class);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(new ErrorResponse(false, "JSON mal formado")));
            out.flush();
            return;
        }

        String hashedPassword = SeguridadUtil.hashearPassword(req.password);
        Usuario nuevoUsuario = new Usuario(req.nombre, req.email, hashedPassword);
        boolean registrado = UsuarioDAO.insertar(nuevoUsuario);

        PrintWriter out = response.getWriter();
        if (registrado) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print(gson.toJson(new SuccessResponse(true, "Registro exitoso")));
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.print(gson.toJson(new ErrorResponse(false, "Error al registrar")));
        }
        out.flush();
    }
}
