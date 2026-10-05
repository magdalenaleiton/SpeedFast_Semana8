package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Centraliza la conexión JDBC a MySQL. */
public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/speedfast_db?useSSL=false&serverTimezone=America/Santiago&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Cambiar si tu MySQL tiene contraseña.

    private ConexionDB() {}

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
