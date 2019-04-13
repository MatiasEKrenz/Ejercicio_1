import java.util.Collection;

public interface IncidenteService {

    public void createIncidente(Incidente incidente);
    public Collection<Incidente> getIncidentes();
    public Incidente getIncidente(int id);
    public Incidente changeDescripcionIncidente (Incidente incidente) throws IncidenteException;
    public Incidente changeEstadoIncidente (int id);
    public Collection<Incidente> getIncidentesAsignadosUsuario (int idUsuario); // responsable
    public Collection<Incidente> getIncidentesCreadosUsuario (int idUsuario); // reportador
    public Collection<Incidente> getIncidentesProyecto (int idProyecto);
    public Collection<Incidente> getIncidentesAbiertos ();
    public Collection<Incidente> getIncidentesCerrados ();

}
