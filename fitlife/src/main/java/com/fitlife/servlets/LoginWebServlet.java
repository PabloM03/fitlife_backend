package com.fitlife.servlets;

import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.utils.SeguridadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name="LoginWeb", urlPatterns={"/login"})
public class LoginWebServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Mostrar formulario
        req.getRequestDispatcher("/login.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        Usuario u = UsuarioDAO.buscarPorEmail(email);
        boolean ok = u != null &&
                     u.getPassword().equals(SeguridadUtil.hashearPassword(password));

        if (ok) {
            HttpSession session = req.getSession(true);
            session.setAttribute("usuario", u);
            // redirige a la página principal
            resp.sendRedirect(req.getContextPath() + "/main.jsp");
        } else {
            req.setAttribute("mensaje", "Correo o contraseña incorrectos.");
            req.getRequestDispatcher("/login.jsp")
               .forward(req, resp);
        }
    }
}
