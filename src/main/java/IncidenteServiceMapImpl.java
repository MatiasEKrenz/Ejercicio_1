import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IncidenteServiceMapImpl implements IncidenteService {

    private HashMap<Integer, Incidente> incidenteMap;

    public IncidenteServiceMapImpl() {
        incidenteMap = new HashMap<>();
    }

    @Override
    public void createIncidente(Incidente incidente) {
        incidenteMap.put(incidente.getId(), incidente);
    }

    @Override
    public Collection<Incidente> getIncidentes() {
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

        for(Map.Entry<Integer, Incidente> entry : incidenteMap.entrySet()) {
            Incidente value = entry.getValue();

            if (value.getId() == id) {
                value.setEstado(Estado.RESUELTO);
                return value;
            }
        }
        return null;
    }

    @Override
    public Collection<Incidente> getIncidentesAsignadosUsuario(int idUsuario) {

        HashMap<Integer, Incidente> auxMap = new HashMap<>();

        for(Map.Entry<Integer, Incidente> entry : incidenteMap.entrySet()) {
            int key = entry.getKey();
            Incidente value = entry.getValue();

            if (value.getResponsable().getId() == idUsuario) {
                auxMap.put(key, value);
            }
        }
        return auxMap.values();
    }

    @Override
    public Collection<Incidente> getIncidentesCreadosUsuario(int idUsuario) {

        HashMap<Integer, Incidente> auxMap = new HashMap<>();

        for(Map.Entry<Integer, Incidente> entry : incidenteMap.entrySet()) {
            int key = entry.getKey();
            Incidente value = entry.getValue();

            if (value.getReportador().getId() == idUsuario) {
                auxMap.put(key, value);
            }
        }
        return auxMap.values();
    }

    @Override
    public Collection<Incidente> getIncidentesProyecto(int idProyecto) {

        HashMap<Integer, Incidente> auxMap = new HashMap<>();

        for(Map.Entry<Integer, Incidente> entry : incidenteMap.entrySet()) {
            int key = entry.getKey();
            Incidente value = entry.getValue();

            if (value.getProyecto().getId() == idProyecto) {
                auxMap.put(key, value);
            }
        }
        return auxMap.values();
    }

    @Override
    public Collection<Incidente> getIncidentesAbiertos() {

        HashMap<Integer, Incidente> auxMap = new HashMap<>();

        for(Map.Entry<Integer, Incidente> entry : incidenteMap.entrySet()) {
            int key = entry.getKey();
            Incidente value = entry.getValue();

            if (value.getEstado() == Estado.ASIGNADO) {
                auxMap.put(key, value);
            }
        }
        return auxMap.values();
    }

    @Override
    public Collection<Incidente> getIncidentesCerrados() {

        HashMap<Integer, Incidente> auxMap = new HashMap<>();

        for(Map.Entry<Integer, Incidente> entry : incidenteMap.entrySet()) {
            int key = entry.getKey();
            Incidente value = entry.getValue();

            if (value.getEstado() == Estado.RESUELTO) {
                auxMap.put(key, value);
            }
        }
        return auxMap.values();
    }
}
