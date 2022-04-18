package br.com.meucampestre.meucampestre.services.interfaces;

import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;

import java.util.List;

public interface IUsuarioService {

    Usuario salvarUsuario(Usuario usuario);

    Papel salvarPapel(Papel papel);

    void adicionarPapelAoUsuario(String documento, String nomePapel);

    Usuario buscarUsuarioPeloDocumento(String documento);

    List<Usuario> buscarUsuarios();

    Usuario atualizarUsuario(Usuario usuario);
}
