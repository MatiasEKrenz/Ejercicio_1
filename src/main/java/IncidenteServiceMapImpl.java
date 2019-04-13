import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IncidenteServiceMapImpl implements IncidenteService {

    private HashMap<Integer, Incidente> incidenteMap;

    public IncidenteServiceMapImpl() {
        incidenteMap = new HashMap<Integer, Incidente>();
    }

    @Override
    public void createIncidente(Incidente incidente) {
        incidenteMap.put(incidente.getId(), incidente);
    }

    @Override
    public Collection<Incidente> getIncidente() {
        return incidenteMap.values();
    }

    @Override
    public Incidente getIncidente(int id) {
        return incidenteMap.get(id);
    }

    @Override
    public Incidente changeDescripcionIncidente(Incidente incidente) throws IncidenteException {
        try {
            if (incidente.getId() == 0) {
                throw new IncidenteException("El id del incidente no puede ser nulo.");
            }
            Incidente incidenteEditar = incidenteMap.get(incidente.getId());
            // a partir de ahora, modifico

            if (incidente.getDescripcion() != null) {
                incidenteEditar.setDescripcion(incidente.getDescripcion());
            }

            return incidenteEditar;

        } catch (Exception exception) {
            throw new IncidenteException(exception.getMessage());
        }
    }

    @Override
    public Incidente changeEstadoIncidente(int id) {

       // Incidente indicenteARetornar = new Incidente();
        for(Map.Entry<Integer, Incidente> entry : incidenteMap.entrySet()) {
            int key = entry.getKey();
            Incidente value = entry.getValue();

            if (value.getId() == id) {
                value.setEstado(Estado.RESUELTO);
            //    indicenteARetornar = value;
            }
        }

       // return indicenteARetornar;
        return null;
    }

    @Override
    public Collection<Incidente> getIncidentesAsignadosUsuario(int idUsuario) {
        return null;
    }

    @Override
    public Collection<Incidente> getIncidentesCreadosUsuario(int idUsuario) {
        return null;
    }

    @Override
    public Collection<Incidente> getIncidentesProyecto(int idProyecto) {
        return null;
    }

    @Override
    public Collection<Incidente> getIncidentesAbiertos() {
        return null;
    }

    @Override
    public Collection<Incidente> getIncidentesPCerrados() {
        return null;
    }
}
