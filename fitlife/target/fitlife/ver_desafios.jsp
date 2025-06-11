<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, com.fitlife.classes.Desafio" %>
<html>
<head><title>🏆 Desafíos Disponibles</title></head>
<body>
  <h2>🏆 Desafíos Disponibles</h2>
  <table border="1" cellpadding="5">
    <tr><th>Título</th><th>Inicio</th><th>Fin</th><th>Acción</th></tr>
    <%
      List<Desafio> retos = (List<Desafio>) request.getAttribute("desafios");
      for (Desafio d : retos) {
    %>
    <tr>
      <td><%= d.getTitulo() %></td>
      <td><%= d.getFechaInicio() %></td>
      <td><%= d.getFechaFin() %></td>
      <td>
        <form action="unirseDesafio" method="post" style="display:inline;">
          <input type="hidden" name="desafioId" value="<%= d.getId() %>" />
          <button type="submit">Unirse</button>
        </form>
      </td>
    </tr>
    <% } %>
  </table>
</body>
</html>
