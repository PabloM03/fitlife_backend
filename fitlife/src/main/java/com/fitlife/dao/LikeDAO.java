// File: LikeDAO.java
package com.fitlife.dao;

import java.sql.*;
import com.fitlife.bd.ConexionBD;

public class LikeDAO {

    /**
     * Inserta un like de un usuario a una publicación.
     * @return true si se insertó correctamente, false si ya existía o error.
     */
    public boolean darLike(int usuarioId, int comidaId) {
        if (existeLike(usuarioId, comidaId)) {
            return false;
        }
        String sql = "INSERT INTO LIKES_COMIDA(usuario_id, comida_id) VALUES (?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, comidaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un like de un usuario a una publicación.
     * @return true si se eliminó, false si no existía o error.
     */
    public boolean quitarLike(int usuarioId, int comidaId) {
        String sql = "DELETE FROM LIKES_COMIDA WHERE usuario_id = ? AND comida_id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, comidaId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Comprueba si un usuario ya ha dado like a una publicación.
     */
    public boolean existeLike(int usuarioId, int comidaId) {
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
