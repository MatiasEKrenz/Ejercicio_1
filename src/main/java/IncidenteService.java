import java.util.Collection;

public interface IncidenteService {

    public void createIncidente(Incidente incidente);
    public Collection<Incidente> getIncidente();
    public Incidente getIncidente(int id);
    public Incidente changeDescripcionIncidente (Incidente incidente) throws IncidenteException;
    public Incidente changeEstadoIncidente (int id) throws IncidenteException;
    public Collection<Incidente> getIncidentesAsignadosUsuario (int idUsuario);
    public Collection<Incidente> getIncidentesCreadosUsuario (int idUsuario);
    public Collection<Incidente> getIncidentesProyecto (int idProyecto);
    public Collection<Incidente> getIncidentesAbiertos ();
    public Collection<Incidente> getIncidentesPCerrados ();

}
