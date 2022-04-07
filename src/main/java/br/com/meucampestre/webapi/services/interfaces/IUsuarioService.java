package br.com.meucampestre.webapi.services.interfaces;

import br.com.meucampestre.webapi.dto.usuario.UsuarioDTO;
import br.com.meucampestre.webapi.entities.Usuario;
import org.springframework.stereotype.Service;

public interface IUsuarioService {

    UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO);

    Usuario buscarUsuarioPorEmail(String email);
}
