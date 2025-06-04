package com.fitlife.dao;

import com.fitlife.bd.ConexionBD;
import com.fitlife.classes.Comida;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComidaDAO {

    public static boolean guardarComida(Comida c) {
        String sql = "INSERT INTO COMIDAS (usuario_id, fecha, nombre, calorias, carbohidratos, proteinas, grasas, observaciones) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, c.getUsuarioId());
            stmt.setDate(2, c.getFecha());
            stmt.setString(3, c.getNombre());
            stmt.setInt(4, c.getCalorias());
            stmt.setDouble(5, c.getCarbohidratos());
            stmt.setDouble(6, c.getProteinas());
            stmt.setDouble(7, c.getGrasas());
            stmt.setString(8, c.getObservaciones());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Comida> obtenerPorUsuarioYFecha(int usuarioId, Date fecha) {
        List<Comida> lista = new ArrayList<>();
        String sql = "SELECT * FROM COMIDAS WHERE usuario_id = ? AND fecha = ? ORDER BY id";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setDate(2, fecha);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Comida c = new Comida();
                c.setId(rs.getInt("id"));
                c.setUsuarioId(usuarioId);
                c.setFecha(fecha);
                c.setNombre(rs.getString("nombre"));
                c.setCalorias(rs.getInt("calorias"));
                c.setCarbohidratos(rs.getDouble("carbohidratos"));
                c.setProteinas(rs.getDouble("proteinas"));
                c.setGrasas(rs.getDouble("grasas"));
                c.setObservaciones(rs.getString("observaciones"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static int totalCalorias(int usuarioId, Date fecha) {
        String sql = "SELECT SUM(calorias) as total FROM COMIDAS WHERE usuario_id = ? AND fecha = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setDate(2, fecha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
