<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, com.fitlife.classes.Comida, com.fitlife.classes.Usuario" %>
<%
  Usuario u = (Usuario) session.getAttribute("usuario");
  if (u == null) {
      response.sendRedirect("login.jsp");
      return;
  }

  List<Comida> comidas = (List<Comida>) request.getAttribute("comidas");
  if (comidas == null) {
      comidas = new java.util.ArrayList<>();
  }

  Integer totalAttr = (Integer) request.getAttribute("totalCalorias");
  int total = totalAttr != null ? totalAttr : 0;
  Integer recAttr   = (Integer) request.getAttribute("caloriasRec");
  int rec   = recAttr   != null ? recAttr   : 0;
%>

<h2>📋 Comidas de hoy (<%= u.getNombre() %>)</h2>
<p>Has consumido <strong><%= total %></strong> kcal de <strong><%= rec %></strong> kcal recomendadas.</p>

<table border="1" cellpadding="5">
  <tr>
    <th>Nombre</th><th>Calorías</th><th>Carb. (g)</th><th>Prot. (g)</th><th>Grasas (g)</th><th>Obs.</th>
  </tr>
  <% for (Comida c : comidas) { %>
    <tr>
      <td><%= c.getNombre() %></td>
      <td><%= c.getCalorias() %></td>
      <td><%= c.getCarbohidratos() %></td>
      <td><%= c.getProteinas() %></td>
      <td><%= c.getGrasas() %></td>
      <td><%= c.getObservaciones() %></td>
    </tr>
  <% } %>
</table>

<p><strong>Total:</strong> <%= total %> kcal</p>
<p><strong>Recomendadas:</strong> <%= rec %> kcal</p>
<p style="color:<%= (total > rec ? "red" : "green") %>;">
  <%= (total > rec ? "🚨 Te has pasado hoy" : "✅ Dentro del rango") %>
</p>

<p>
  <a href="registrarComida">Agregar otra comida</a>
  |
  <a href="main.jsp">← Volver al panel principal</a>
</p>
