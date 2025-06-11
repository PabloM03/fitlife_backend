<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fitlife.classes.Usuario" %>
<%@ page import="com.fitlife.classes.Rutina" %>
<html>
<head>
    <title>Rutina Asignada</title>
</head>
<body>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Rutina rutina = (Rutina) request.getAttribute("rutinaAsignada");
%>

<h2>🏋️ Rutina Asignada para <%= usuario.getNombre() %></h2>

<%
    if (rutina == null) {
%>
    <p>No tienes ninguna rutina asignada actualmente.</p>
    <p><a href="verRutinas">👉 Seleccionar una rutina</a></p>
<%
    } else {
%>
    <p><strong>Nombre:</strong> <%= rutina.getNombre() %></p>
    <p><strong>Nivel:</strong> <%= rutina.getNivel() %></p>
    <p><strong>Descripción:</strong><br/> <%= rutina.getDescripcion() %></p>
<%
    }
%>

<br/>
<p><a href="main.jsp">← Volver al panel principal</a></p>

</body>
</html>
