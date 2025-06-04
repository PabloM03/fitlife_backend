package com.fitlife.dao;

import com.fitlife.bd.ConexionBD;
import com.fitlife.classes.Rutina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RutinaDAO {

    // Obtener todas las rutinas del sistema
    public static List<Rutina> obtenerTodas() {
        List<Rutina> lista = new ArrayList<>();
        String sql = "SELECT * FROM RUTINAS";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Rutina r = new Rutina();
                r.setId(rs.getInt("id"));
                r.setNombre(rs.getString("nombre"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setNivel(rs.getString("nivel"));
                lista.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Obtener la rutina asignada a un usuario
    public static Rutina obtenerPorUsuarioId(int usuarioId) {
        String sql = """
            SELECT r.*
            FROM RUTINAS r
            JOIN usuario_rutina ur ON r.id = ur.rutina_id
            WHERE ur.usuario_id = ?
        """;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Rutina r = new Rutina();
                r.setId(rs.getInt("id"));
                r.setNombre(rs.getString("nombre"));
                r.setDescripcion(rs.getString("descripcion"));
                r.setNivel(rs.getString("nivel"));
                return r;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Asignar una rutina a un usuario (reemplaza la actual si existe)
    public static boolean asignarRutina(int usuarioId, int rutinaId) {
        String delete = "DELETE FROM usuario_rutina WHERE usuario_id = ?";
        String insert = "INSERT INTO usuario_rutina (usuario_id, rutina_id) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement delStmt = conn.prepareStatement(delete);
             PreparedStatement insStmt = conn.prepareStatement(insert)) {

            // Eliminar rutina anterior si existe
            delStmt.setInt(1, usuarioId);
            delStmt.executeUpdate();

            // Insertar nueva asignación
            insStmt.setInt(1, usuarioId);
            insStmt.setInt(2, rutinaId);
            int filas = insStmt.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
