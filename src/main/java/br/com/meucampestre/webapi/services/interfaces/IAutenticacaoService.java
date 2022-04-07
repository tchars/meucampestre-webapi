package br.com.meucampestre.webapi.services.interfaces;

import br.com.meucampestre.webapi.dto.autenticacao.CriarNovoUsuarioDTO;
import br.com.meucampestre.webapi.dto.autenticacao.LoginDTO;
import br.com.meucampestre.webapi.dto.autenticacao.JWTAuthResponseDTO;
import br.com.meucampestre.webapi.dto.usuario.UsuarioDTO;

public interface IAutenticacaoService {

    JWTAuthResponseDTO autenticarUsuario(LoginDTO usuario);

    CriarNovoUsuarioDTO registrarNovoUsuario(UsuarioDTO registroDTO);
}
