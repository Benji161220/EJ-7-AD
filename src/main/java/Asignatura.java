import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Asignatura {
    private String nombre;
    private String aula;
    private boolean obligatoria;
}
