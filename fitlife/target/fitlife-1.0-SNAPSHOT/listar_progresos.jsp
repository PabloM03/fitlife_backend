<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="com.fitlife.classes.Progreso" %>
<%@ page import="com.fitlife.classes.Usuario" %>
<%@ page import="com.fitlife.utils.NutricionUtil" %>

<html>
<head>
    <title>Historial de Progreso</title>
</head>
<body></body>
    <%
        Usuario u = (Usuario) session.getAttribute("usuario");
        if (u == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Recuperar atributos del servlet
        List<Progreso> progresos = (List<Progreso>) request.getAttribute("progresos");
        LocalDate desde = (LocalDate) request.getAttribute("desde");
        LocalDate hasta = (LocalDate) request.getAttribute("hasta");

        int caloriasRecomendadas = NutricionUtil.getCaloriasRecomendadas(u);
    %>

    <h2>📈 Monitoreo de Progreso de <%= u.getNombre() %></h2>

    <form method="get" action="listarProgresos">
        Desde: <input type="date" name="desde" value="<%= desde != null ? desde : "" %>" />
        Hasta: <input type="date" name="hasta" value="<%= hasta != null ? hasta : "" %>" />
        <button type="submit">Filtrar</button>
    </form>

    <% if (u.getNivelActividad() != null) { %>
        <h3>🔥 Calorías recomendadas por día: <%= caloriasRecomendadas %> kcal</h3>
    <% } else { %>
        <p style="color:orange;">⚠️ Aún no has configurado tu nivel de actividad.</p>
    <% } %>

    <% if (progresos == null || progresos.isEmpty()) { %>
        <p style="color: darkred; font-weight: bold;">🛑 Aún no has registrado ningún progreso.</p>
        <p>Puedes hacerlo desde el panel principal usando la opción <strong>“📈 Registrar Progreso”</strong>.</p>
    <% } else { %>
        <table border="1" cellpadding="8">
            <tr>
                <th>Fecha</th>
                <th>Peso (kg)</th>
                <th>Calorías</th>
                <th>Observaciones</th>
            </tr>
            <% for (Progreso p : progresos) { %>
                <tr>
                    <td><%= p.getFecha() %></td>
                    <td><%= p.getPeso() %></td>
                    <td><%= p.getCalorias() %></td>
                    <td><%= p.getObservaciones() %></td>
                </tr>
            <% } %>
        </table>
    <% } %>

    <br />
    <p><a href="main.jsp">← Volver al panel principal</a></p>

</body>
</html>