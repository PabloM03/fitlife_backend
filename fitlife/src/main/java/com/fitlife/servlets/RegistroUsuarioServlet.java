package com.fitlife.servlets;

import com.fitlife.classes.Usuario;
import com.fitlife.dao.UsuarioDAO;
import com.fitlife.enums.NivelActividad;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/registro")
public class RegistroUsuarioServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("[SERVLET] ➤ Registro de nuevo usuario iniciado.");

        try {
            // Capturar todos los parámetros
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String sexo = request.getParameter("sexo");
            String objetivo = request.getParameter("objetivo");
            String nivelActividadStr = request.getParameter("nivel_actividad");

            int edad = Integer.parseInt(request.getParameter("edad"));
            double peso = Double.parseDouble(request.getParameter("peso"));
            double altura = Double.parseDouble(request.getParameter("altura"));

            NivelActividad nivelActividad = NivelActividad.fromString(nivelActividadStr);


            // Crear y configurar el usuario
            Usuario nuevoUsuario = new Usuario(0, nombre, email, password);
            nuevoUsuario.setSexo(sexo);
            nuevoUsuario.setObjetivo(objetivo);
            nuevoUsuario.setEdad(edad);
            nuevoUsuario.setPeso(peso);
            nuevoUsuario.setAltura(altura);
            nuevoUsuario.setNivelActividad(nivelActividad);

            // Registrar
            boolean exito = UsuarioDAO.registrarUsuario(nuevoUsuario);

            if (exito) {
                System.out.println("[SERVLET] ✅ Registro exitoso.");
                request.setAttribute("mensaje", "¡Registro exitoso!");
            } else {
                System.out.println("[SERVLET] ❌ Registro fallido (email duplicado).");
                request.setAttribute("mensaje", "Error: El correo ya está registrado.");
            }

        } catch (Exception e) {
            System.err.println("[SERVLET] ❌ Error durante el registro:");
            e.printStackTrace();
        
            request.setAttribute("mensaje", "Error en el servidor. Intenta nuevamente.");
            request.setAttribute("excepcion", e);  // 👉 pasamos la excepción completa
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
        

        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("registro.jsp");
    }
}
