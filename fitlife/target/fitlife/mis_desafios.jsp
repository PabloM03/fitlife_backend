<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, com.fitlife.classes.Participacion, com.fitlife.classes.Desafio, com.fitlife.dao.DesafioDAO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mis Desafíos</title>
    <style>
      table { border-collapse: collapse; width: 100%; }
      th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
      th { background: #f0f0f0; }
    </style>
</head>
<body>
    <h2>💪 Mis Desafíos</h2>

    <%  
        List<Participacion> participaciones =
            (List<Participacion>) request.getAttribute("partici");

        if (participaciones == null || participaciones.isEmpty()) {
    %>
        <p>No estás inscrito en ningún desafío por el momento.</p>
    <%
        } else {
    %>
    <table>
        <tr>
            <th>Título</th>
            <th>Unido el</th>
            <th>Completado</th>
            <th>Acciones</th>
        </tr>
        <% for (Participacion p : participaciones) {
               Desafio d = DesafioDAO.obtener(p.getDesafioId());
        %>
        <tr>
            <td><%= d.getTitulo() %></td>
            <td><%= p.getFechaJoin() %></td>  <!-- Aquí el getter correcto -->
            <td><%= p.isCompletado() ? "✅ Sí" : "❌ No" %></td>
            <td>
                <% if (!p.isCompletado()) { %>
                    <a href="completarDesafio?id=<%= p.getId() %>">Marcar como completado</a>
                <% } else { %>
                    —
                <% } %>
            </td>
        </tr>
        <% } %>
    </table>
    <%
        }
    %>

    <p><a href="desafios">← Volver a Desafíos Disponibles</a></p>
    <p><a href="main.jsp">← Inicio</a></p>
</body>
</html>
