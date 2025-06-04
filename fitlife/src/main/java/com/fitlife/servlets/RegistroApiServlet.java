package com.fitlife.servlets;

import com.google.gson.Gson;
import com.fitlife.api.RegisterRequest;
import com.fitlife.api.RegisterResponse;
import com.fitlife.api.ErrorResponse;
import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.enums.NivelActividad;
import com.fitlife.utils.SeguridadUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

@WebServlet(name = "RegisterAPI", urlPatterns = {"/api/register"})
public class RegistroApiServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // --- Leer JSON de entrada ---
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        // --- Parsear a RegisterRequest ---
        RegisterRequest r;
        try {
            r = gson.fromJson(sb.toString(), RegisterRequest.class);
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(
                gson.toJson(new ErrorResponse(false, "JSON mal formado"))
            );
            return;
        }

        // --- Validar y convertir nivel de actividad ---
        final NivelActividad nivelActividad;
        try {
            nivelActividad = NivelActividad.fromString(r.nivelActividad);
        } catch (IllegalArgumentException ex) {
            // Lista de valores permitidos
            String allowed = Arrays.stream(NivelActividad.values())
                                   .map(Enum::name)
                                   .collect(Collectors.joining(", "));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(
                gson.toJson(new ErrorResponse(
                    false,
                    "Nivel de actividad inválido: '" + r.nivelActividad + 
                    "'. Valores permitidos: " + allowed
                ))
            );
            return;
        }

        // --- Construir usuario con hash de contraseña ---
        Usuario u = new Usuario(
            0,
            r.nombre,
            r.email,
            SeguridadUtil.hashearPassword(r.password)
        );
        u.setSexo(r.sexo);
        u.setObjetivo(r.objetivo);
        u.setEdad(r.edad);
        u.setPeso(r.peso);
        u.setAltura(r.altura);
        u.setNivelActividad(nivelActividad);

        // --- Intentar registro en BD ---
        boolean ok;
        try {
            ok = UsuarioDAO.registrarUsuario(u);
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(
                gson.toJson(new ErrorResponse(false, "Error interno del servidor"))
            );
            return;
        }

        PrintWriter out = resp.getWriter();
        if (ok) {
            Usuario creado = UsuarioDAO.buscarPorEmail(r.email);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.print(
                gson.toJson(new RegisterResponse(
                    true,
                    "Registro exitoso",
                    creado != null ? creado.getId() : -1
                ))
            );
        } else {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            out.print(
                gson.toJson(new ErrorResponse(false, "El correo ya está registrado"))
            );
        }
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(
            gson.toJson(new ErrorResponse(false, "Método no permitido en /api/register"))
        );
    }
}
