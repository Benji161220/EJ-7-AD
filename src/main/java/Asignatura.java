public class Asignatura {
    private int id;
    private String nombre;
    private String aula;
    private boolean obligatoria;
    public Asignatura(){}
    public Asignatura(int id, String nombre, String aula, boolean obligatoria) {
        this.id = id;
        this.nombre = nombre;
        this.aula = aula;
        this.obligatoria = obligatoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public boolean isObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(boolean obligatoria) {
        this.obligatoria = obligatoria;
    }
    public String toString() {
        return (id+", "+nombre+", "+aula+", "+obligatoria);
    }
}
