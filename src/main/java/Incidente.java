import java.util.Date;

public class Incidente {

    private int id;
    private Clasificacion clasificacion;
    private String descripcion;
    private Usuario reportador;
    private Usuario responsable;
    private Estado estado;
    private Date fechaDeCreacion;
    private Date fechaDeSolucion;
    private Proyecto proyecto;

    public Incidente(int id, Clasificacion clasificacion, String descripcion, Usuario reportador,
                     Usuario responsable, Estado estado, Proyecto proyecto) {
        this.id = id;
        this.clasificacion = clasificacion;
        this.descripcion = descripcion;
        this.reportador = reportador;
        this.responsable = responsable;
        this.estado = estado;
        this.proyecto = proyecto;

        fechaDeCreacion = new Date();
        fechaDeSolucion = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getReportador() {
        return reportador;
    }

    public void setReportador(Usuario reportador) {
        this.reportador = reportador;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(Date fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public Date getFechaDeSolucion() {
        return fechaDeSolucion;
    }

    public void setFechaDeSolucion(Date fechaDeSolucion) {
        this.fechaDeSolucion = fechaDeSolucion;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}
