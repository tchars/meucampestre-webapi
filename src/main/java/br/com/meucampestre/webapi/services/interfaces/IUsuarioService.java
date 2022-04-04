package br.com.meucampestre.webapi.services.interfaces;

import br.com.meucampestre.webapi.dto.usuario.UsuarioDTO;
import org.springframework.stereotype.Service;

public interface IUsuarioService {

    UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO);
}
