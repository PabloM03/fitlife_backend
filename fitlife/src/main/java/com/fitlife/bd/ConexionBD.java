package com.fitlife.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/fitlife";
    private static final String USER = "salva";
    private static final String PASS = "1234";


    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver MySQL cargado correctamente.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: Driver MySQL no encontrado.");
            e.printStackTrace();
        }

        try (Connection conn = getConnection()) {
            System.out.println("✅ Conexión a MySQL establecida correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos:");
            System.err.println("URL: " + URL);
            System.err.println("Usuario: " + USER);
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
