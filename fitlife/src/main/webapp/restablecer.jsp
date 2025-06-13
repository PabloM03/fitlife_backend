<%
    String token = (String) request.getAttribute("token");
%>

<h2>Restablecer contraseña</h2>

<p>Por favor, copia el siguiente token y pégalo en la app para continuar:</p>

<p style="font-weight: bold; font-size: 20px; color: darkblue;">
    <%= token != null ? token : "Token no disponible" %>
</p>
