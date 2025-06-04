<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Recuperar Contraseña</title>
</head>
<body>

<h2>🔐 Recuperar Contraseña</h2>

<form method="post" action="recuperar">
    Introduce tu correo electrónico:<br/><br/>
    <input type="email" name="email" required /><br/><br/>
    <input type="submit" value="Enviar instrucciones" />
</form>

<p style="color: green;">
    <%= request.getAttribute("mensaje") != null ? request.getAttribute("mensaje") : "" %>
</p>

<p><a href="inicio.jsp">← Volver al inicio</a></p>

</body>
</html>
