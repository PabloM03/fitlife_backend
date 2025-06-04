package com.fitlife.dao;

import com.fitlife.bd.ConexionBD;
import com.fitlife.classes.RutinaDia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RutinaDiaDAO {

    // Obtener todos los días de una rutina
    public static List<RutinaDia> obtenerDiasPorRutina(int rutinaId) {
        List<RutinaDia> lista = new ArrayList<>();
        String sql = "SELECT * FROM RUTINA_DIAS WHERE rutina_id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            stmt.setInt(1, rutinaId);
            try (ResultSet rs2 = stmt.executeQuery()) {
                while (rs2.next()) {
                    RutinaDia dia = new RutinaDia();
                    dia.setId(rs2.getInt("id"));
                    dia.setRutinaId(rs2.getInt("rutina_id"));
                    dia.setDiaSemana(rs2.getString("dia_semana"));
                    dia.setDescripcion(rs2.getString("descripcion"));
                    lista.add(dia);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Obtener solo el día actual de una rutina
    public static RutinaDia obtenerDiaActual(int rutinaId, String diaSemana) {
        String sql = "SELECT * FROM RUTINA_DIAS WHERE rutina_id = ? AND dia_semana = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rutinaId);
            stmt.setString(2, diaSemana);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    RutinaDia dia = new RutinaDia();
                    dia.setId(rs.getInt("id"));
                    dia.setRutinaId(rs.getInt("rutina_id"));
                    dia.setDiaSemana(rs.getString("dia_semana"));
                    dia.setDescripcion(rs.getString("descripcion"));
                    return dia;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
