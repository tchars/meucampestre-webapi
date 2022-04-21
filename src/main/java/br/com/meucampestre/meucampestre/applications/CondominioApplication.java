package br.com.meucampestre.meucampestre.applications;

import br.com.meucampestre.meucampestre.apimodels.condominio.CriarCondominioRequest;
import br.com.meucampestre.meucampestre.apimodels.condominio.CriarCondominioResponse;
import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioRequest;
import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioResponse;
import br.com.meucampestre.meucampestre.domain.constants.TiposDePapeis;
import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.domain.models.UsuarioPapelCondominioLink;
import br.com.meucampestre.meucampestre.repositories.UsuarioPapelCondominioLinkRepo;
import br.com.meucampestre.meucampestre.services.interfaces.ICondominioService;
import br.com.meucampestre.meucampestre.services.interfaces.IPapelService;
import br.com.meucampestre.meucampestre.services.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CondominioApplication {

    private final IUsuarioService _usuarioService;
    private final ICondominioService _condominioService;
    private final IPapelService _papelService;
    private final PasswordEncoder _passwordEncoder;
    private final UsuarioPapelCondominioLinkRepo _usuarioPapelCondominioLinkRepo;

    // Contexto - CONDOMÍNIO
    public CriarCondominioResponse criarCondominio(CriarCondominioRequest request)
    {
        Papel papel = _papelService.buscarPapelPeloNome(TiposDePapeis.CONDOMINIO);

        Condominio condominio = new Condominio(null, request.getNome(),
                request.getEmail(), request.getSenha(), request.getDocumento(),
                request.getDescricao(), papel, null, null,
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

        condominio.setSenha(_passwordEncoder.encode(condominio.getSenha()));

        Condominio condominoCriado = _condominioService.salvarCondominio(condominio);

        return new CriarCondominioResponse(condominoCriado.getId(), condominoCriado.getEmail(),
                condominoCriado.getDocumento(), condominoCriado.getDescricao(),
                condominoCriado.getCriadoEm());
    }

    public List<Condominio> buscarTodosCondominios()
    {
        return _condominioService.buscarTodosCondominios();
    }

    public Condominio buscarCondominio(Long idCondominio)
    {
        return _condominioService.buscarCondominio(idCondominio);
    }

    // CONTEXTO USUARIO
    public Collection<Usuario> buscarTodosUsuariosDeUmCondominio(Long idCondominio)
    {
        return _condominioService.buscarTodosUsuariosDeUmCondominio(idCondominio);
    }

    public CriarUsuarioResponse criarUsuario(Long idCondominio,
                                             CriarUsuarioRequest request)
    {
        Usuario usuario = _usuarioService.buscarUsuarioPeloDocumento(request.getDocumento());
        Papel papelDesejado = _papelService.buscarPapelPeloNome(request.getPapel());
        Condominio condominio = _condominioService.buscarCondominio(idCondominio);

        if (papelDesejado == null) {
            throw new RuntimeException("Permissão/Role não encontrada");
        }

        if (condominio == null) {
            throw new RuntimeException("Condomínio não encontrado");
        }

        // Usuário não existe no sistema
        if (usuario == null)
        {
            // Cadastro
            usuario = _usuarioService.salvarUsuario(cadastrarNovoUsuario(request, papelDesejado));

            _usuarioPapelCondominioLinkRepo.save(new UsuarioPapelCondominioLink(null,
                    usuario, condominio, papelDesejado));

            return mapParaResponse(usuario, condominio);
        }

        // Se o usuário já existe no sistema
        // verifico se ele já existe no condomínio
        UsuarioPapelCondominioLink usuarioJaExisteComARoleNesteCondominio =
                _usuarioPapelCondominioLinkRepo
                        .buscarPorUsuarioCondominioPapel(usuario.getId(), condominio.getId(),
                                papelDesejado.getId());

        // Se ele não tiver relação entre usuário - condomínio - role pedida, posso criar
        if (usuarioJaExisteComARoleNesteCondominio == null)
        {
            _usuarioPapelCondominioLinkRepo.save(new UsuarioPapelCondominioLink(null,
                    usuario, condominio, papelDesejado));

            return mapParaResponse(usuario, condominio);
        }

        throw new RuntimeException("Usuário já possui cadastro neste condomínio com esta role");
    }

    private CriarUsuarioResponse mapParaResponse(Usuario usuario, Condominio condominio)
    {
        return new CriarUsuarioResponse(usuario.getId(),
                usuario.getNome(),
                usuario.getDocumento(),
                usuario.getPapeis().stream().collect(Collectors.toList()),
                condominio.getId()
        );
    }

    private Usuario cadastrarNovoUsuario(CriarUsuarioRequest request,
                                      Papel papel)
    {
        Usuario usuario = new Usuario(null,
                request.getNome(),
                request.getSenha(),
                request.getDocumento(),
                new ArrayList<>(),
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()));

        usuario.getPapeis().add(papel);

        return usuario;
    }
}
