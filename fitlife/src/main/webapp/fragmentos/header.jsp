<%@ page import="com.fitlife.classes.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario != null) {
%>
    <p>👋 Bienvenido, <strong><%= usuario.getNombre() %></strong></p>
    <form action="logout" method="post">
        <button type="submit">Cerrar sesión</button>
    </form>
<%
    }
%>
