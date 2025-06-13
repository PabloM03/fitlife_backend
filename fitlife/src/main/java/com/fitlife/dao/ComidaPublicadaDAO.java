// File: ComidaPublicadaDAO.java
package com.fitlife.dao;

import com.fitlife.api.ComidaFeedResponse;
import java.sql.*;
import java.util.*;
import com.fitlife.bd.ConexionBD;

public class ComidaPublicadaDAO {

    /**
     * Inserta una nueva publicación de comida.
     * @return true si la inserción fue exitosa.
     */
    public boolean insertarPublicacion(int usuarioId,
                                       String nombre,
                                       String descripcion,
                                       int calorias,
                                       double carbohidratos,
                                       double proteinas,
                                       double grasas,
                                       String fotoPath) {
        String sql = "INSERT INTO COMIDAS_PUBLICADAS(usuario_id, nombre, descripcion, calorias, carbohidratos, proteinas, grasas, foto_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setInt(4, calorias);
            ps.setDouble(5, carbohidratos);
            ps.setDouble(6, proteinas);
            ps.setDouble(7, grasas);
            ps.setString(8, fotoPath);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera el feed de comidas ordenado por fecha (descendente).
     * Incluye información de likes y si el usuario ya ha dado like.
     */
    public List<ComidaFeedResponse> listarFeed(int usuarioId) {
        List<ComidaFeedResponse> lista = new ArrayList<>();
        String sql = "SELECT p.id, u.nombre AS usuarioNombre, p.foto_path, p.nombre AS nombreComida, " +
                     "p.calorias, p.carbohidratos, p.proteinas, p.grasas, p.descripcion, p.fecha_pub " +
                     "FROM COMIDAS_PUBLICADAS p " +
                     "JOIN USUARIOS u ON p.usuario_id = u.id " +
                     "ORDER BY p.fecha_pub DESC";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ComidaFeedResponse item = new ComidaFeedResponse();
                int pubId = rs.getInt("id");
                item.setIdPublicacion(pubId);
                item.setUsuarioNombre(rs.getString("usuarioNombre"));
                item.setFotoURL(rs.getString("foto_path"));
                item.setNombreComida(rs.getString("nombreComida"));
                item.setCalorias(rs.getInt("calorias"));
                item.setCarbohidratos(rs.getDouble("carbohidratos"));
                item.setProteinas(rs.getDouble("proteinas"));
                item.setGrasas(rs.getDouble("grasas"));
                item.setDescripcion(rs.getString("descripcion"));
                item.setFechaPublicacion(rs.getTimestamp("fecha_pub"));

                // Likes
                int totalLikes = contarLikes(pubId);
                item.setLikes(totalLikes);
                boolean liked = haDadoLike(usuarioId, pubId);
                item.setHaDadoLike(liked);

                lista.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private int contarLikes(int comidaId) {
        String sql = "SELECT COUNT(*) FROM LIKES_COMIDA WHERE comida_id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comidaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean haDadoLike(int usuarioId, int comidaId) {
        String sql = "SELECT 1 FROM LIKES_COMIDA WHERE usuario_id = ? AND comida_id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, comidaId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}