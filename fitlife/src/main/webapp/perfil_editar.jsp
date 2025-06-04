<%@ page import="com.fitlife.enums.NivelActividad" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="com.fitlife.classes.Usuario" %>
        <html>

        <head>
            <title>Editar Perfil</title>
        </head>

        <body>

            <% Usuario usuario=(Usuario) session.getAttribute("usuario"); if (usuario !=null) { %>
                <h2>✏️ Editar Perfil</h2>

                <form method="post" action="editarPerfil">
                    Edad: <input type="number" name="edad" value="<%= usuario.getEdad() %>" required /><br /><br />
                    Peso (kg): <input type="number" step="0.1" name="peso" value="<%= usuario.getPeso() %>"
                        required /><br /><br />
                    Altura (cm): <input type="number" step="0.1" name="altura" value="<%= usuario.getAltura() %>"
                        required /><br /><br />

                    Nivel de actividad:
                    <select name="nivelActividad" required>
                        <option value="SEDENTARIO" <%=usuario.getNivelActividad()==NivelActividad.SEDENTARIO
                            ? "selected" : "" %>>Sedentario</option>
                        <option value="LIGERAMENTE_ACTIVO"
                            <%=usuario.getNivelActividad()==NivelActividad.LIGERAMENTE_ACTIVO ? "selected" : "" %>
                            >Ligeramente activo</option>
                        <option value="MODERADAMENTE_ACTIVO"
                            <%=usuario.getNivelActividad()==NivelActividad.MODERADAMENTE_ACTIVO ? "selected" : "" %>
                            >Moderadamente activo</option>
                        <option value="MUY_ACTIVO" <%=usuario.getNivelActividad()==NivelActividad.MUY_ACTIVO
                            ? "selected" : "" %>>Muy activo</option>
                        <option value="EXTRA_ACTIVO" <%=usuario.getNivelActividad()==NivelActividad.EXTRA_ACTIVO
                            ? "selected" : "" %>>Extra activo</option>
                    </select>
                    <br /><br />


                    Objetivo:
                    <select name="objetivo" required>
                        <option <%="Perder peso" .equals(usuario.getObjetivo()) ? "selected" : "" %>>Perder peso
                        </option>
                        <option <%="Ganar músculo" .equals(usuario.getObjetivo()) ? "selected" : "" %>>Ganar músculo
                        </option>
                        <option <%="Mantenerse en forma" .equals(usuario.getObjetivo()) ? "selected" : "" %>>Mantenerse
                            en forma</option>
                    </select><br /><br />

                    <input type="submit" value="Guardar Cambios" />
                </form>

                <p><a href="perfil">← Volver al perfil</a></p>

                <% } else { %>
                    <p>No has iniciado sesión. <a href="login.jsp">Inicia sesión aquí</a></p>
                    <% } %>

        </body>

        </html>