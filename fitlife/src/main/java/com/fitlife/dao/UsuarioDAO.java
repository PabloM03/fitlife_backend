package com.fitlife.dao;

import com.fitlife.bd.ConexionBD;
import com.fitlife.classes.Usuario;
import com.fitlife.enums.NivelActividad;
import com.fitlife.utils.SeguridadUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public static boolean registrarUsuario(Usuario usuario) {
        System.out.println("[INFO] Intentando registrar usuario con email: " + usuario.getEmail());

        Usuario existente = buscarPorEmail(usuario.getEmail());
        if (existente != null) {
            System.out.println("[ERROR] Ya existe un usuario con ese correo: " + existente.getEmail());
            return false;
        }

        String sql = "INSERT INTO USUARIOS (nombre, email, password, edad, peso, altura, nivel_actividad, objetivo, sexo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getPassword()); // ✅ Ya está hasheada en el servlet
            stmt.setInt(4, usuario.getEdad());
            stmt.setDouble(5, usuario.getPeso());
            stmt.setDouble(6, usuario.getAltura());
            stmt.setString(7, usuario.getNivelActividad() != null ? usuario.getNivelActividad().name() : null); 
            stmt.setString(8, usuario.getObjetivo());
            stmt.setString(9, usuario.getSexo());

            int filas = stmt.executeUpdate();
            System.out.println("[SUCCESS] Usuario insertado correctamente. Filas afectadas: " + filas);
            return true;

        } catch (SQLException e) {
            System.out.println("[ERROR] Error al registrar usuario:");
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario buscarPorEmail(String email) {
        System.out.println("[DEBUG] Buscando usuario con email: " + email);

        String sql = "SELECT * FROM USUARIOS WHERE email = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("[DEBUG] Usuario encontrado en BD: " + rs.getString("email"));

                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password")); // Hashed
                usuario.setEdad(rs.getInt("edad"));
                usuario.setPeso(rs.getDouble("peso"));
                usuario.setAltura(rs.getDouble("altura"));

                String nivelActividadDb = rs.getString("nivel_actividad");
                if (nivelActividadDb != null) {
                    usuario.setNivelActividad(NivelActividad.fromString(rs.getString("nivel_actividad")));
                }

                usuario.setObjetivo(rs.getString("objetivo"));
                usuario.setSexo(rs.getString("sexo"));

                return usuario;
            } else {
                System.out.println("[DEBUG] Usuario NO encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Error al buscar usuario por email:");
            e.printStackTrace();
        }

        return null;
    }

    public static List<Usuario> getUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM USUARIOS";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setPassword(rs.getString("password"));
                usuario.setEdad(rs.getInt("edad"));
                usuario.setPeso(rs.getDouble("peso"));
                usuario.setAltura(rs.getDouble("altura"));

                String nivelActividadDb = rs.getString("nivel_actividad");
                if (nivelActividadDb != null) {
                    usuario.setNivelActividad(NivelActividad.valueOf(nivelActividadDb));
                }

                usuario.setObjetivo(rs.getString("objetivo"));
                usuario.setSexo(rs.getString("sexo"));
                lista.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE USUARIOS SET edad = ?, peso = ?, altura = ?, nivel_actividad = ?, objetivo = ? WHERE email = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuario.getEdad());
            stmt.setDouble(2, usuario.getPeso());
            stmt.setDouble(3, usuario.getAltura());
            stmt.setString(4, usuario.getNivelActividad() != null ? usuario.getNivelActividad().name() : null);
            stmt.setString(5, usuario.getObjetivo());
            stmt.setString(6, usuario.getEmail());

            int filas = stmt.executeUpdate();
            System.out.println("[UPDATE] Usuario actualizado. Filas modificadas: " + filas);
            return filas > 0;

        } catch (SQLException e) {
            System.out.println("[ERROR] Error al actualizar usuario:");
            e.printStackTrace();
            return false;
        }
    }
}
