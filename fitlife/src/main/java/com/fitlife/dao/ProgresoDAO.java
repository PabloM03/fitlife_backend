package com.fitlife.dao;

import com.fitlife.bd.ConexionBD;
import com.fitlife.classes.Progreso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgresoDAO {

    public static boolean guardarProgreso(Progreso progreso) {
        String sqlInsert = "INSERT INTO PROGRESOS (usuario_id, fecha, peso, calorias, observaciones) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, progreso.getUsuarioId());
            stmt.setDate(2, progreso.getFecha());
            stmt.setDouble(3, progreso.getPeso());
            stmt.setInt(4, progreso.getCalorias());
            stmt.setString(5, progreso.getObservaciones());

            int filas = stmt.executeUpdate();
            System.out.println("[PROGRESO] Registro guardado. Filas afectadas: " + filas);

            if (filas > 0) {
                // 🟢 Obtener el ID generado del progreso insertado
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int progresoIdInsertado = generatedKeys.getInt(1);
                    System.out.println("[INFO] ID del progreso insertado: " + progresoIdInsertado);

                    // 🟠 Obtener el progreso más reciente (por fecha DESC, id DESC)
                    Progreso ultimoProgreso = obtenerUltimoProgreso(progreso.getUsuarioId());

                    if (ultimoProgreso != null &&
                            progreso.getFecha().equals(ultimoProgreso.getFecha()) &&
                            progresoIdInsertado == ultimoProgreso.getId()) {

                        System.out.println("[INFO] Es el progreso más reciente. Se actualizará el peso del perfil.");
                        actualizarPesoUsuario(progreso.getUsuarioId(), progreso.getPeso());
                    } else {
                        System.out.println("[INFO] No es el progreso más reciente. No se actualiza el peso.");
                    }
                }
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Error al guardar progreso:");
            e.printStackTrace();
            return false;
        }
    }

    // 🟠 Obtener el progreso más reciente (por fecha y luego id DESC)
    private static Progreso obtenerUltimoProgreso(int usuarioId) {
        String sql = "SELECT * FROM PROGRESOS WHERE usuario_id = ? ORDER BY fecha DESC, id DESC LIMIT 1";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Progreso p = new Progreso();
                p.setId(rs.getInt("id"));
                p.setUsuarioId(rs.getInt("usuario_id"));
                p.setFecha(rs.getDate("fecha"));
                p.setPeso(rs.getDouble("peso"));
                p.setCalorias(rs.getInt("calorias"));
                p.setObservaciones(rs.getString("observaciones"));
                return p;
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Error al obtener el último progreso:");
            e.printStackTrace();
        }

        return null;
    }

    private static void actualizarPesoUsuario(int usuarioId, double nuevoPeso) {
        String sql = "UPDATE usuarios SET peso = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, nuevoPeso);
            stmt.setInt(2, usuarioId);
            int filas = stmt.executeUpdate();
            System.out.println("[USUARIO] Peso actualizado en perfil. Filas afectadas: " + filas);

        } catch (SQLException e) {
            System.out.println("[ERROR] Error al actualizar el peso en el perfil del usuario:");
            e.printStackTrace();
        }
    }

    public static List<Progreso> obtenerProgresosPorUsuario(int usuarioId) {
        List<Progreso> lista = new ArrayList<>();
        String sql = "SELECT * FROM PROGRESOS WHERE usuario_id = ? ORDER BY fecha DESC";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Progreso p = new Progreso();
                p.setId(rs.getInt("id"));
                p.setUsuarioId(rs.getInt("usuario_id"));
                p.setFecha(rs.getDate("fecha"));
                p.setPeso(rs.getDouble("peso"));
                p.setCalorias(rs.getInt("calorias"));
                p.setObservaciones(rs.getString("observaciones"));
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static List<Progreso> obtenerProgresosPorUsuarioPeriodo(int usuarioId, Date desde, Date hasta) {
        List<Progreso> lista = new ArrayList<>();
        String sql = "SELECT * FROM PROGRESOS WHERE usuario_id = ? AND fecha BETWEEN ? AND ? ORDER BY fecha ASC";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.setDate(2, desde);
            stmt.setDate(3, hasta);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Progreso p = new Progreso();
                p.setId(rs.getInt("id"));
                p.setUsuarioId(usuarioId);
                p.setFecha(rs.getDate("fecha"));
                p.setPeso(rs.getDouble("peso"));
                p.setCalorias(rs.getInt("calorias"));
                p.setObservaciones(rs.getString("observaciones"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
