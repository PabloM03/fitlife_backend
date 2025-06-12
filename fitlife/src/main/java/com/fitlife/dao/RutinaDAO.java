package com.fitlife.dao;

import com.fitlife.bd.ConexionBD;
import com.fitlife.classes.Rutina;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class RutinaDAO {
    private static final Logger LOGGER = Logger.getLogger(RutinaDAO.class.getName());

    static {
        try {
            String catalinaBase = System.getProperty("catalina.base", ".");
            Path logDir = Paths.get(catalinaBase, "logs", "fitlife");
            Files.createDirectories(logDir);
            Path logFile = logDir.resolve("rutina-dao.log");
            FileHandler fh = new FileHandler(logFile.toString(), true);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Obtener todas las rutinas del sistema
    public static List<Rutina> obtenerTodas() {
        List<Rutina> lista = new ArrayList<>();
        String sql = "SELECT * FROM RUTINAS";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Rutina r = new Rutina();
                r.setId(rs.getInt("ID"));
                r.setNombre(rs.getString("NOMBRE"));
                r.setDescripcion(rs.getString("DESCRIPCION"));
                r.setNivel(rs.getString("NIVEL"));
                lista.add(r);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todas las rutinas", e);
        }

        return lista;
    }

    // Obtener la rutina asignada a un usuario
    public static Rutina obtenerPorUsuarioId(int usuarioId) {
        String sql = """
            SELECT r.*
              FROM RUTINAS r
              JOIN USUARIO_RUTINA ur ON r.ID = ur.RUTINA_ID
             WHERE ur.USUARIO_ID = ?
        """;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rutina r = new Rutina();
                    r.setId(rs.getInt("ID"));
                    r.setNombre(rs.getString("NOMBRE"));
                    r.setDescripcion(rs.getString("DESCRIPCION"));
                    r.setNivel(rs.getString("NIVEL"));
                    return r;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener rutina por USUARIO_ID=" + usuarioId, e);
        }

        return null;
    }

    // Asignar una rutina a un usuario (reemplaza la actual si existe)
    public static boolean asignarRutina(int usuarioId, int rutinaId) {
        String sqlUpdate = """
            UPDATE USUARIO_RUTINA
               SET RUTINA_ID = ?, FECHA_ASIGNACION = CURDATE()
             WHERE USUARIO_ID = ?
        """;
        String sqlInsert = """
            INSERT INTO USUARIO_RUTINA (USUARIO_ID, RUTINA_ID, FECHA_ASIGNACION)
            VALUES (?, ?, CURDATE())
        """;

        Connection conn = null;
        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);

            LOGGER.info("Intentando UPDATE USUARIO_RUTINA para USUARIO_ID=" + usuarioId + ", nueva RUTINA_ID=" + rutinaId);
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setInt(1, rutinaId);
                ps.setInt(2, usuarioId);
                int updated = ps.executeUpdate();
                LOGGER.info("UPDATE afectó filas: " + updated);
                if (updated > 0) {
                    conn.commit();
                    LOGGER.info("UPDATE commit realizado.");
                    return true;
                }
            }

            LOGGER.info("No había registro previo, intentando INSERT en USUARIO_RUTINA para USUARIO_ID=" + usuarioId + ", RUTINA_ID=" + rutinaId);
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, usuarioId);
                ps.setInt(2, rutinaId);
                int inserted = ps.executeUpdate();
                LOGGER.info("INSERT afectó filas: " + inserted);
                if (inserted > 0) {
                    conn.commit();
                    LOGGER.info("INSERT commit realizado.");
                    return true;
                }
            }

            conn.rollback();
            LOGGER.warning("Ni UPDATE ni INSERT afectaron filas; rollback realizado.");
            return false;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error al hacer rollback en asignarRutina", ex);
            }
            LOGGER.log(Level.SEVERE, "Error en asignarRutina USUARIO_ID=" + usuarioId + ", RUTINA_ID=" + rutinaId, e);
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error al cerrar conexión en asignarRutina", e);
                }
            }
        }
    }
}
