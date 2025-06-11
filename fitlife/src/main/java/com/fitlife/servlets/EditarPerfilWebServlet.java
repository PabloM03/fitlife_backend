package com.fitlife.servlets;

import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.enums.NivelActividad;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/editarPerfil")
public class EditarPerfilWebServlet extends HttpServlet {

    // Mostrar el formulario de edición
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Redirigir al formulario de edición
        request.getRequestDispatcher("perfil_editar.jsp").forward(request, response);
    }

    // Guardar los cambios del perfil
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            usuario.setEdad(Integer.parseInt(request.getParameter("edad")));
            usuario.setPeso(Double.parseDouble(request.getParameter("peso")));
            usuario.setAltura(Double.parseDouble(request.getParameter("altura")));
            String nivelActividadParam = request.getParameter("nivelActividad");
            if (nivelActividadParam != null && !nivelActividadParam.isEmpty()) {
                usuario.setNivelActividad(NivelActividad.valueOf(nivelActividadParam));
            } else {
                usuario.setNivelActividad(null); 
            }

            usuario.setObjetivo(request.getParameter("objetivo"));

            boolean actualizado = UsuarioDAO.actualizarUsuario(usuario);
            if (actualizado) {
                System.out.println("[SUCCESS] Usuario actualizado correctamente en la base de datos.");
            } else {
                System.out.println("[WARNING] No se actualizó ninguna fila.");
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Fallo al actualizar el perfil:");
            e.printStackTrace();
        }

        response.sendRedirect("perfil");
    }
}
