import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class Profesor {
    private String nombre;
    private String apellido;
    private LocalDate fechaInicio;
}
