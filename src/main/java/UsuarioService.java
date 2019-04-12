import java.util.Collection;

public interface UsuarioService {

    public void addUsuario(Usuario usuario);
    public Collection<Usuario> getUsuarios();
    public Usuario getUsuario(int id);
    public Usuario editUsuario(Usuario usuario) throws UsuarioException;
    public void deleteUsuario(int id);

}
