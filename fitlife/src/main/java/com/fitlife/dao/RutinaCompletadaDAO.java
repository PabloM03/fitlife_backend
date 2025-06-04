package com.fitlife.dao;

import com.fitlife.bd.ConexionBD;
import com.fitlife.classes.RutinaCompletada;

import java.sql.*;
import java.time.LocalDate;

public class RutinaCompletadaDAO {

    // Verificar si el usuario ya completó hoy su rutina
    public static boolean yaCompletadoHoy(int usuarioId, int rutinaId, String diaSemana) {
        String sql = "SELECT COUNT(*) FROM RUTINAS_COMPLETADAS WHERE usuario_id = ? AND rutina_id = ? AND fecha = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setInt(2, rutinaId);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Insertar rutina como completada
    public static boolean registrarCompletado(RutinaCompletada rutinaCompletada) {
        String sql = """
            INSERT INTO RUTINAS_COMPLETADAS (usuario_id, rutina_id, fecha, dia_semana, descripcion, completado)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rutinaCompletada.getUsuarioId());
            stmt.setInt(2, rutinaCompletada.getRutinaId());
            stmt.setDate(3, rutinaCompletada.getFecha());
            stmt.setString(4, rutinaCompletada.getDiaSemana());
            stmt.setString(5, rutinaCompletada.getDescripcion());
            stmt.setBoolean(6, rutinaCompletada.isCompletado());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
