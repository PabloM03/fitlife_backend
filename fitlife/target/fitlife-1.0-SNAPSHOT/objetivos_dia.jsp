<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fitlife.classes.Usuario" %>
<%@ page import="com.fitlife.classes.Rutina" %>
<%@ page import="com.fitlife.classes.RutinaDia" %>
<%@ page import="com.fitlife.dao.RutinaCompletadaDAO" %>

<html>
<head>
    <title>Objetivos del Día</title>
</head>
<body>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String mensaje = (String) request.getAttribute("mensaje");
    Rutina rutina = (Rutina) request.getAttribute("rutina");
    RutinaDia ejercicioDelDia = (RutinaDia) request.getAttribute("ejercicioDelDia");
    String nombreDia = (String) request.getAttribute("nombreDia");
%>

<h2>🎯 Objetivos del Día - <%= nombreDia %></h2>

<%
    if (mensaje != null) {
%>
    <p><%= mensaje %></p>
<%
    } else if (ejercicioDelDia != null) {
%>
    <p><strong>Rutina:</strong> <%= rutina.getNombre() %> (<%= rutina.getNivel() %>)</p>
    <p><strong>Ejercicio de hoy:</strong></p>
    <p><%= ejercicioDelDia.getDescripcion() %></p>

    <%
        // Verificar si ya completó la rutina hoy
        boolean yaCompletado = RutinaCompletadaDAO.yaCompletadoHoy(
            usuario.getId(), rutina.getId(), nombreDia
        );
        if (!yaCompletado) {
    %>
        <form method="post" action="completarRutina">
            <button type="submit">✅ Marcar rutina como completada</button>
        </form>
    <%
        } else {
    %>
        <p style="color: green; font-weight: bold;">
            🎉 ¡Ya has completado tu rutina de hoy!
        </p>
    <%
        }
    %>

<%
    } else {
%>
    <p>No hay actividad programada para hoy. ¡Podés tomarlo como un día de descanso! 😌</p>
<%
    }
%>

<br/>
<p><a href="main.jsp">← Volver al panel principal</a></p>

</body>
</html>
