package br.com.meucampestre.meucampestre.applications;

import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioRequest;
import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioResponse;
import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.services.interfaces.ICondominioService;
import br.com.meucampestre.meucampestre.services.interfaces.IPapelService;
import br.com.meucampestre.meucampestre.services.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioApplication {

    private final IUsuarioService _usuarioService;
    private final PasswordEncoder _passwordEncoder;
    private final IPapelService _papelService;
    private final ICondominioService _condominioService;

    public List<Usuario> buscarTodosUsuarios() {
        return _usuarioService.buscarUsuarios();
    }

//    public CriarUsuarioResponse criarUsuario(CriarUsuarioRequest request)
//    {
//        Usuario usuario = _usuarioService.buscarUsuarioPeloDocumento(request.getDocumento());
//        Papel papel = _papelService.buscarPapelPeloNome(request.getPapel());
//        Condominio condominio = _condominioService.buscarCondominio(request.getIdCondominio());
//
//        if (usuario != null) {
//            throw new RuntimeException("Usuário já existe");
//        }
//
//        if (papel == null) {
//            throw new RuntimeException("Permissão/Role não encontrada");
//        }
//
//        if (condominio == null) {
//            throw new RuntimeException("Condomínio não encontrado");
//        }
//
//        // Salvando o usuário
//        usuario = new Usuario(null, request.getNome(), request.getSenha(),
//                request.getDocumento(), new ArrayList<>(), new Date(System.currentTimeMillis()),
//                new Date(System.currentTimeMillis()));
//
//        usuario.setSenha(_passwordEncoder.encode(request.getSenha()));
//
//        Usuario usuarioCriado = _usuarioService.salvarUsuario(usuario);
//
//        // Atribuindo ele a um condomínio
//
//        return new CriarUsuarioResponse(usuarioCriado.getId(), usuarioCriado.getNome(),
//                usuarioCriado.getDocumento(), usuarioCriado.getPapeis().toArray().toString());
//    }
}
