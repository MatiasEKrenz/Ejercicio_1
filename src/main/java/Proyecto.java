public class Proyecto {

    private int id;
    private String titulo;
    private Usuario propietario;

    public Proyecto(int id, String titulo, Usuario propietario) {
        this.id = id;
        this.titulo = titulo;
        this.propietario = propietario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }
}
