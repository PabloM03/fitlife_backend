<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fitlife.classes.Usuario" %>
<%
    // Context path para URLs dinámicas
    String context = request.getContextPath();
    // Usuario en sesión (si ya inició sesión)
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    // Mensaje informativo (registrado en request por el servlet)
    String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión – FitLife</title>
</head>
<body>

<% if (usuario != null) { %>
    <!-- Usuario ya logueado -->
    <p>👋 Bienvenido, <strong><%= usuario.getNombre() %></strong></p>
    <form action="<%= context %>/logout" method="post">
        <button type="submit">Cerrar sesión</button>
    </form>
    <p><a href="<%= context %>/main.jsp">Ir al panel principal</a></p>

<% } else { %>
    <!-- Formulario de login -->
    <h2>Iniciar sesión en FitLife</h2>

    <% if (mensaje != null) { %>
        <p style="color: green;"><%= mensaje %></p>
    <% } %>

    <form method="post" action="<%= context %>/login">
        <label for="email">Email:</label><br/>
        <input type="email" id="email" name="email" required /><br/><br/>

        <label for="password">Contraseña:</label><br/>
        <input type="password" id="password" name="password" required /><br/><br/>

        <button type="submit">Entrar</button>
    </form>

    <p>¿No tienes cuenta? <a href="<%= context %>/registro.jsp">Regístrate aquí</a></p>
    <p><a href="<%= context %>/recuperar.jsp">¿Olvidaste tu contraseña?</a></p>
    <p><a href="<%= context %>/inicio.jsp">← Volver al inicio</a></p>
<% } %>

</body>
</html>
