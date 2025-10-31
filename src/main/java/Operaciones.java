import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class Operaciones {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    // Cargar las propiedades de conexión
    public static void main(String[] args) {
        loadDatabaseProperties();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            crearProfesorYAsignatura(conn,
                    new Asignatura("Arte Muggle","2A",false),
                    new Profesor("Albus","Dumbledore", LocalDate.of(2025,10,31)));
            crearProfesorYAsignatura(conn,
                    new Asignatura("Defensa Contra las Artes Oscuras", "3C",true),
                    new Profesor("Minerva","McGonagall", LocalDate.of(1956,9,1)));
            System.out.println();
            Estudiante selena = new Estudiante("Selena", "Shade", 4, LocalDate.parse("2007-05-23"));
            funcionMatricularEstudiante(conn, selena);
            System.out.println();

            Estudiante theo = new Estudiante("Theo", "Blackthorn", 3, LocalDate.parse("2008-10-11"));
            procedimientoMatricularEstudiante(conn, theo);
            System.out.println();

            System.out.println("Todos los ejercicios ejecutados correctamente.");
        } catch (SQLException e) {
        System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
    public static void procedimientoMatricularEstudiante(Connection conn, Estudiante estudiante) {
        String sql = "CALL crear_estudiante(?::VARCHAR, ?::VARCHAR, ?::DATE, ?::INT)";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, estudiante.getNombre());
            cstmt.setString(2, estudiante.getApellido());
            cstmt.setDate(3, Date.valueOf(estudiante.getFechaNacimiento()));
            cstmt.setInt(4, estudiante.getAnyoCurso());
            cstmt.execute();
            System.out.println("Procedimiento crear_estudiante ejecutado con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al ejecutar el procedimiento crear_estudiante: " + e.getMessage());
        }
    }
    public static void funcionMatricularEstudiante(Connection conn, Estudiante estudiante) {
        String sql = "SELECT * FROM matricular_estudiante(?::VARCHAR, ?::VARCHAR, ?::DATE, ?::INT)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setDate(3, Date.valueOf(estudiante.getFechaNacimiento()));
            pstmt.setInt(4, estudiante.getAnyoCurso());

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("=== Resultado función matricular_estudiante ===");
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i <= cols; i++) {
                        if (i > 1) sb.append(" | ");
                        sb.append(md.getColumnLabel(i)).append(": ").append(rs.getString(i));
                    }
                    System.out.println(sb);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la función matricular_estudiante: " + e.getMessage());
        }
    }

    private static int crearProfesorYAsignatura(Connection conn, Asignatura asignatura, Profesor profesor){
        int idGenerado = -1;  // Para almacenar el ID del nuevo profesor
        try {
            // Comenzar la transacción
            conn.setAutoCommit(false);

            String sqlProfesor = "INSERT INTO profesor (nombre, apellido, fecha_inicio) VALUES (?, ?, ?)";
            String sqlAsignatura = "INSERT INTO asignatura (id_profesor, nombre_asignatura, aula, obligatoria) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstProfesor = conn.prepareStatement(sqlProfesor, Statement.RETURN_GENERATED_KEYS)) {
                pstProfesor.setString(1, profesor.getNombre());
                pstProfesor.setString(2, profesor.getApellido());
                pstProfesor.setDate(3, Date.valueOf(profesor.getFechaInicio()));
                int filasAfectadasProfesor = pstProfesor.executeUpdate();
                System.out.println("Filas afectadas al profesor: " + filasAfectadasProfesor);
                // Obtener el ID generado
                try (ResultSet rsProfesor = pstProfesor.getGeneratedKeys()) {
                    if (rsProfesor.next()) {
                        idGenerado = rsProfesor.getInt(1);
                    } else {
                        throw new SQLException("Error al obtener ID del nuevo usuario.");
                    }
                }
            }
            // Inserción en la tabla asignatura
            try (PreparedStatement pstAsignatura = conn.prepareStatement(sqlAsignatura)) {
                pstAsignatura.setInt(1, idGenerado);
                pstAsignatura.setString(2, asignatura.getNombre());
                pstAsignatura.setString(3, asignatura.getAula());
                pstAsignatura.setBoolean(4, asignatura.isObligatoria());

                int filasAfectadasCuenta = pstAsignatura.executeUpdate();
                System.out.println("Filas afectadas al insertar cuenta: " + filasAfectadasCuenta);
            }

            // Confirmar la transacción
            conn.commit();
            System.out.println("Profesor y Asignatura insertados con éxito. ID del nuevo profesor: " + idGenerado);
        } catch (SQLException e) {
            try {
                // Si algo falla, hacemos rollback
                conn.rollback();
                System.out.println("Transacción fallida. Haciendo rollback.");
            } catch (SQLException rollbackEx) {
                System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                // Restaurar el modo autocommit al final
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
        return idGenerado;
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
