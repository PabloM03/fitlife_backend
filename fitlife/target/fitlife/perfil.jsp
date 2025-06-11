<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fitlife.classes.Usuario" %>
<html>
<head>
    <title>Mi Perfil</title>
</head>
<body>

<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    if (usuario != null) {
%>
    <h2>👤 Perfil del Usuario</h2>

    <p><strong>Nombre:</strong> <%= usuario.getNombre() %></p>
    <p><strong>Email:</strong> <%= usuario.getEmail() %></p>

    <h3>📊 Datos detallados</h3>
    <p><strong>Edad:</strong> <%= usuario.getEdad() %> años</p>
    <p><strong>Peso:</strong> <%= usuario.getPeso() %> kg</p>
    <p><strong>Altura:</strong> <%= usuario.getAltura() %> cm</p>
    <p><strong>Nivel de Actividad:</strong> <%= usuario.getNivelActividad() %></p>
    <p><strong>Objetivo:</strong> <%= usuario.getObjetivo() %></p>

    <form action="editarPerfil" method="get">
        <button type="submit">Editar Información</button>
    </form>

    <p><a href="main.jsp">← Volver al panel principal</a></p>

    <form action="logout" method="post">
        <button type="submit">Cerrar sesión</button>
    </form>
<%
    } else {
%>
    <p>No has iniciado sesión. <a href="login.jsp">Inicia sesión aquí</a></p>
<%
    }
%>

</body>
</html>
