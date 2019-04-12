import java.util.Collection;
import java.util.HashMap;

public class UsuarioServiceMapImpl implements UsuarioService {

    private HashMap<Integer, Usuario> usuarioMap;

    public UsuarioServiceMapImpl() {
        usuarioMap = new HashMap<Integer, Usuario>();
    }

    @Override
    public void addUsuario(Usuario usuario) {
        usuarioMap.put(usuario.getId(), usuario);
    }

    @Override
    public Collection<Usuario> getUsuarios() {
        return usuarioMap.values();
    }

    @Override
    public Usuario getUsuario(int id) {
        return usuarioMap.get(id);
    }

    @Override
    public Usuario editUsuario(Usuario usuario) throws UsuarioException {
        try {
            if (usuario.getId() == 0) {
                throw new UsuarioException("El id del usuario no puede ser nulo.");
            }
            Usuario usuarioEditar = usuarioMap.get(usuario.getId());
            // a partir de ahora, modifico

            if (usuario.getNombre() != null) {
                usuarioEditar.setNombre(usuario.getNombre());
            }

            if (usuario.getApellido() != null) {
                usuarioEditar.setApellido(usuario.getApellido());
            }

            return usuarioEditar;

        } catch (Exception exception) {
            throw new UsuarioException(exception.getMessage());
        }
    }

    @Override
    public void deleteUsuario(int id) {
        usuarioMap.remove(id);
    }
}
