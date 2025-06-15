package com.fitlife.servlets;

import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.utils.SeguridadUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;

@WebServlet(name = "RegistroUsuario", urlPatterns = {"/registro"})
public class RegistroUsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String nombre = req.getParameter("nombre");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        String hashedPassword = SeguridadUtil.hashearPassword(password);
        Usuario usuario = new Usuario(nombre, email, hashedPassword);
        boolean exito = UsuarioDAO.insertar(usuario);

        if (exito) {
            resp.sendRedirect("login.jsp");
        } else {
            resp.sendRedirect("registro.jsp?error=true");
        }
    }
}
