package com.fitlife.dao;

import com.fitlife.bd.ConexionBD;
import com.fitlife.classes.Participacion;
import java.sql.*;
import java.util.*;
import java.sql.Date;

public class ParticipacionDAO {
    public static boolean unirse(int usuarioId, int desafioId) {
        String sql = "INSERT INTO PARTICIPACIONES (usuario_id, desafio_id, fecha_join) VALUES (?,?,?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, usuarioId);
            p.setInt(2, desafioId);
            p.setDate(3, Date.valueOf(java.time.LocalDate.now()));
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean abandonar(int usuarioId, int desafioId) {
        String sql = "DELETE FROM PARTICIPACIONES WHERE usuario_id=? AND desafio_id=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, usuarioId);
            p.setInt(2, desafioId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static List<Participacion> listarPorUsuario(int usuarioId) {
        List<Participacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM PARTICIPACIONES WHERE usuario_id=? ORDER BY fecha_join";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, usuarioId);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Participacion pt = new Participacion();
                pt.setId(rs.getInt("id"));
                pt.setUsuarioId(usuarioId);
                pt.setDesafioId(rs.getInt("desafio_id"));
                pt.setFechaJoin(rs.getDate("fecha_join"));
                pt.setCompletado(rs.getBoolean("completado"));
                lista.add(pt);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public static boolean marcarCompletado(int usuarioId, int desafioId) {
        String sql = "UPDATE PARTICIPACIONES SET completado=TRUE WHERE usuario_id=? AND desafio_id=?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, usuarioId);
            p.setInt(2, desafioId);
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
