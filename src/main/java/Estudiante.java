import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Estudiante {
    private String nombre;
    private String apellido;
    private int anyoCurso;
    private LocalDate fechaNacimiento;
}