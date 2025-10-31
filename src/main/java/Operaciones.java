import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class Operaciones {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    // Cargar las propiedades de conexión
    public static void main(String[] args) {
        loadDatabaseProperties();
        Asignatura asignatura = new Asignatura("Arte Muggle","2A",false);
        Profesor profesor = new Profesor("Albus","Dumbledore",LocalDate.of(2025,10,31));
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            crearProfesorYAsignatura(conn, asignatura, profesor);
        } catch (SQLException e) {
        System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
    private static void crearProfesorYAsignatura(Connection conn, Asignatura asignatura, Profesor profesor){

    }
    private static void loadDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream input = Operaciones.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(input);
            URL = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASSWORD = properties.getProperty("db.password");
        } catch (IOException ex) {
            System.err.println("Error al cargar el archivo de propiedades: " + ex.getMessage());
        }
    }
}
