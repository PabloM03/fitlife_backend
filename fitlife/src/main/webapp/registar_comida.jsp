<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.fitlife.classes.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head>
    <title>Registrar Comida</title>
</head>
<body>
    <h2>🍽️ Registrar comida para <%= usuario.getNombre() %></h2>
    <form action="registrarComida" method="post">
        Nombre: <input name="nombre" required/><br/><br/>
        Calorías: <input name="calorias" type="number" required/><br/><br/>
        Carbohidratos (g): <input name="carbohidratos" type="number" step="0.1"/><br/><br/>
        Proteínas (g):    <input name="proteinas"    type="number" step="0.1"/><br/><br/>
        Grasas (g):       <input name="grasas"       type="number" step="0.1"/><br/><br/>
        Observaciones:<br/>
        <textarea name="observaciones" rows="3" cols="40"></textarea><br/><br/>
        <button type="submit">Guardar</button>
    </form>
    <p><a href="main.jsp">← Volver al panel principal</a></p>
</body>
</html>
