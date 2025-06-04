<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fitlife.classes.Usuario" %>
<%@ page import="com.fitlife.classes.Rutina" %>

<html>
<head>
    <title>Rutinas Disponibles</title>
</head>
<body>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Rutina> rutinas = (List<Rutina>) request.getAttribute("rutinas");
    String mensaje = (String) request.getAttribute("mensaje");
%>

<h2>🏋️ Rutinas Disponibles</h2>

<% if (mensaje != null) { %>
    <p style="color: green;"><%= mensaje %></p>
<% } %>

<table border="1" cellpadding="10">
    <tr>
        <th>Nombre</th>
        <th>Descripción</th>
        <th>Nivel</th>
        <th>Acción</th>
    </tr>
    <% for (Rutina r : rutinas) { %>
    <tr>
        <td><%= r.getNombre() %></td>
        <td><%= r.getDescripcion() %></td>
        <td><%= r.getNivel() %></td>
        <td>
            <form method="post" action="asignarRutina">
                <input type="hidden" name="rutinaId" value="<%= r.getId() %>" />
                <input type="submit" value="Seleccionar" />
            </form>
        </td>
    </tr>
    <% } %>
</table>

<br/>
<p><a href="main.jsp">← Volver al panel principal</a></p>

</body>
</html>
