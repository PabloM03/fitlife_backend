<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.fitlife.classes.Usuario" %>
        <html>

        <head>
            <title>Registro de Usuario</title>
        </head>

        <body>

            <% Usuario usuario=(Usuario) session.getAttribute("usuario"); if (usuario !=null) { %>
                <p>Ya has iniciado sesión como <strong>
                        <%= usuario.getNombre() %>
                    </strong>.</p>
                <a href="main.jsp">Ir al panel principal</a>
                <form action="logout" method="post">
                    <button type="submit">Cerrar sesión</button>
                </form>
                <% } else { %>
                    <h2>Registro en FitLife</h2>

                    <form method="post" action="registro">
                        Nombre: <input type="text" name="nombre" required /><br /><br />
                        Email: <input type="email" name="email" required /><br /><br />
                        Contraseña: <input type="password" name="password" required /><br /><br />

                        Edad: <input type="number" name="edad" min="1" required /><br /><br />
                        Peso (kg): <input type="number" name="peso" step="0.1" required /><br /><br />
                        Altura (cm): <input type="number" name="altura" step="0.1" required /><br /><br />

                        Sexo:
                        <select name="sexo" required>
                            <option value="">--Selecciona--</option>
                            <option value="Hombre">Hombre</option>
                            <option value="Mujer">Mujer</option>
                        </select><br /><br />

                        Nivel de Actividad:
                        <select name="nivel_actividad" required>
                            <option value="">--Selecciona--</option>
                            <option value="SEDENTARIO">Sedentario</option>
                            <option value="LIGERAMENTE_ACTIVO">Ligeramente Activo</option>
                            <option value="MODERADAMENTE_ACTIVO">Moderadamente Activo</option>
                            <option value="MUY_ACTIVO">Muy Activo</option>
                            <option value="EXTRA_ACTIVO">Extra Activo</option>
                        </select><br /><br />


                        Objetivo: <input type="text" name="objetivo" required /><br /><br />

                        <input type="submit" value="Registrarse" />
                    </form>

                    <p>¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión</a></p>
                    <p><a href="inicio.jsp">← Volver al inicio</a></p>
                    <% } %>

                        <% String mensaje=(String) request.getAttribute("mensaje"); if (mensaje !=null &&
                            mensaje.contains("Error")) { %>
                            <p style="color: red;">
                                <%= mensaje %>
                            </p>
                            <% } else if (mensaje !=null) { %>
                                <p style="color: green;">
                                    <%= mensaje %>
                                </p>
                                <% } %>

                                    <% Exception ex=(Exception) request.getAttribute("excepcion"); if (ex !=null) { %>
                                        <hr />
                                        <h3 style="color: red;">Error en el servidor (detalle técnico):</h3>
                                        <pre style="background-color: #fdd; padding: 10px;">
<%= ex.toString() %>
<%
        for (StackTraceElement ste : ex.getStackTrace()) {
            out.println(ste.toString());
        }
%>
    </pre>
                                        <% } %>

        </body>

        </html>