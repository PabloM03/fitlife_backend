<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Restablecer Contraseña</title>
</head>
<body>

<%
    String token = request.getParameter("token");
    if (token == null || token.isEmpty()) {
%>
    <p style="color:red;">Token no válido o faltante.</p>
    <a href="inicio.jsp">Volver al inicio</a>
<%
    } else {
%>

<h2>🔑 Restablecer Contraseña</h2>

<form method="post" action="restablecer">
    <input type="hidden" name="token" value="<%= token %>" />
    Nueva contraseña: <br/>
    <input type="password" name="password" required /><br/><br/>
    <input type="submit" value="Guardar nueva contraseña" />
</form>

<%
    }
%>

</body>
</html>
