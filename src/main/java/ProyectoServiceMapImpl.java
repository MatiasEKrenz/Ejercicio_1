import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProyectoServiceMapImpl implements ProyectoService {

    private HashMap<Integer, Proyecto> proyectoMap;

    public ProyectoServiceMapImpl() {
        proyectoMap = new HashMap<Integer, Proyecto>();
    }

    @Override
    public void createProyect(Proyecto proyecto) {
        proyectoMap.put(proyecto.getId(), proyecto);
    }

    @Override
    public Collection<Proyecto> getProyectos() {
        return proyectoMap.values();
    }

    @Override
    public Proyecto getProyecto(int id) {
        return proyectoMap.get(id);
    }

    @Override
    public Proyecto editProyecto(Proyecto proyecto) throws ProyectoException{

        try {
            if (proyecto.getId() == 0) {
                throw new ProyectoException("El id del proyecto no puede ser nulo.");
            }
            Proyecto proyectoEditar = proyectoMap.get(proyecto.getId());
            // a partir de ahora, modifico

            if (proyecto.getTitulo() != null) {
                proyectoEditar.setTitulo(proyecto.getTitulo());
            }

            if (proyecto.getPropietario() != null) {
                proyectoEditar.setPropietario(proyecto.getPropietario());
            }

            return proyectoEditar;

        } catch (Exception exception) {
            throw new ProyectoException(exception.getMessage());
        }
    }

    @Override
    public void deleteProyecto(int id) {
        proyectoMap.remove(id);
    }

    @Override
    public Collection<Proyecto> getProyectosPropietario(int idUsuario) {

        HashMap<Integer, Proyecto> auxMap = new HashMap<Integer, Proyecto>();

        for(Map.Entry<Integer, Proyecto> entry : proyectoMap.entrySet()) {
            int key = entry.getKey();
            Proyecto value = entry.getValue();

            if (value.getPropietario().getId() == idUsuario) {
                auxMap.put(key, value);
            }
        }

        return auxMap.values();
    }
}
