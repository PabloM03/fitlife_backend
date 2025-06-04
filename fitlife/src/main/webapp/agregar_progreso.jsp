<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fitlife.classes.Usuario" %>
<html>
<head>
    <title>Registrar Progreso</title>
</head>
<body>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<h2>📈 Registrar Progreso</h2>

<form method="post" action="agregarProgreso">
    Fecha: <input type="date" name="fecha" required /><br/><br/>
    Peso (kg): <input type="number" step="0.1" name="peso" required /><br/><br/>
    Calorías: <input type="number" name="calorias" required /><br/><br/>
    Observaciones: <br/>
    <textarea name="observaciones" rows="4" cols="40" placeholder="Ej: día de descanso, buena sesión, etc."></textarea><br/><br/>
    <input type="submit" value="Guardar" />
</form>

<p><a href="main.jsp">← Volver al panel principal</a></p>

</body>
</html>
