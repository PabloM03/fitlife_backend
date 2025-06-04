package com.fitlife.dao;

import com.fitlife.bd.ConexionBD;
import com.fitlife.classes.Desafio;
import java.sql.*;
import java.util.*;

public class DesafioDAO {
    public static List<Desafio> listarTodos() {
        List<Desafio> lista = new ArrayList<>();
        String sql = "SELECT * FROM DESAFIOS ORDER BY fecha_inicio";
        try (Connection c = ConexionBD.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                Desafio d = new Desafio();
                d.setId(rs.getInt("id"));
                d.setTitulo(rs.getString("titulo"));
                d.setDescripcion(rs.getString("descripcion"));
                d.setFechaInicio(rs.getDate("fecha_inicio"));
                d.setFechaFin(rs.getDate("fecha_fin"));
                lista.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public static Desafio obtener(int id) {
        String sql = "SELECT * FROM DESAFIOS WHERE id = ?";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                Desafio d = new Desafio();
                d.setId(id);
                d.setTitulo(rs.getString("titulo"));
                d.setDescripcion(rs.getString("descripcion"));
                d.setFechaInicio(rs.getDate("fecha_inicio"));
                d.setFechaFin(rs.getDate("fecha_fin"));
                return d;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static boolean crear(Desafio d) {
        String sql = "INSERT INTO DESAFIOS (titulo, descripcion, fecha_inicio, fecha_fin) VALUES (?,?,?,?)";
        try (Connection c = ConexionBD.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, d.getTitulo());
            p.setString(2, d.getDescripcion());
            p.setDate(3, d.getFechaInicio());
            p.setDate(4, d.getFechaFin());
            return p.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
