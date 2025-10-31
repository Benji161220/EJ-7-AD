import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Profesor {
    private String nombre;
    private String apellido;
    private LocalDate fechaInicio;
}
