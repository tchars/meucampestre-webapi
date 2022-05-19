package br.com.meucampestre.meucampestre.applications;

import br.com.meucampestre.meucampestre.apimodels.usuarios.*;
import br.com.meucampestre.meucampestre.apimodels.usuarios.partials.CondominioResponse;
import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.domain.models.UsuarioPapelCondominio;
import br.com.meucampestre.meucampestre.repositories.UsuarioPapelCondominioRepo;
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
public class UsuarioApplication {

    private final IUsuarioService _usuarioService;
    private final IPapelService _papelService;
    private final ICondominioService _condominioService;
    private final PasswordEncoder _encoder;
    private final UsuarioPapelCondominioRepo _usuarioPapelCondominioRepo;

    public CriarUsuarioResponse criarUsuario(Long idCondominio,
                                             CriarUsuarioRequest request)
    {
        Papel papelDesejado = _papelService.buscarPapelPeloNome(request.getPapel());
        if (papelDesejado == null)
        {
            throw new RuntimeException("Permissão/Role não encontrada");
        }

        Condominio condominio = _condominioService.buscarCondominio(idCondominio);
        if (condominio == null)
        {
            throw new RuntimeException("Condomínio não encontrado");
        }

        Usuario usuarioASerCriado =
                _usuarioService.buscarUsuarioPeloDocumento(request.getDocumento());

        // Usuário não existe no sistema
        if (usuarioASerCriado == null)
        {
            // Cadastro
            usuarioASerCriado = _usuarioService.salvarUsuario(
                    mapearDadosParaNovoUsuario(request, papelDesejado));

            _usuarioPapelCondominioRepo.save(new UsuarioPapelCondominio(null,
                    usuarioASerCriado, condominio, papelDesejado, false));

            return mapParaResponse(usuarioASerCriado, condominio);
        }

        // Se o usuário já existe no sistema
        // verifico se ele já existe no condomínio
        UsuarioPapelCondominio usuarioJaExisteComARoleNesteCondominio =
                _usuarioPapelCondominioRepo
                        .buscarPorUsuarioCondominioPapel(usuarioASerCriado.getId(),
                                condominio.getId(),
                                papelDesejado.getId()
                        );

        // Se ele tiver relação entre usuário - condomínio - role pedida, não posso criar
        if (usuarioJaExisteComARoleNesteCondominio != null)
        {
            throw new RuntimeException("Usuário já possui cadastro neste condomínio com esta role");
        }

        _usuarioPapelCondominioRepo.save(new UsuarioPapelCondominio(
                null,
                usuarioASerCriado,
                condominio,
                papelDesejado, false)
        );

        return mapParaResponse(usuarioASerCriado, condominio);
    }

    public AtualizarUsuarioResponse atualizarPerfilDoUsuario(Long idCondominio,
                                                             AtualizarUsuarioRequest request)
    {
        Usuario usuarioAntigo = _usuarioService.buscarUsuarioPeloDocumento(request.getDocumento());

        if (usuarioAntigo == null)
        {
            throw new RuntimeException("Usuário não encontrado");
        }

        Collection<Papel> papeis = new ArrayList<>();
        for (String papel : request.getPapeis())
        {
            papeis.add(_papelService.buscarPapelPeloNome(papel));
        }

        usuarioAntigo.setNome(request.getNome());
        usuarioAntigo.setEmail(request.getEmail());

        if (!request.getSenha().isEmpty())
        {
            usuarioAntigo.setSenha(_encoder.encode(request.getSenha()));
        }

        usuarioAntigo.setTelefone(request.getTelefone());
        usuarioAntigo.setImagemUrl(request.getImagemUrl());

        usuarioAntigo.setPapeis(papeis);

        _usuarioService.salvarUsuario(usuarioAntigo);

        _usuarioPapelCondominioRepo.apagarTodasPermissoesDoUsuarioAoCondominio(usuarioAntigo.getId(), idCondominio);

        for (Papel pp : papeis)
        {
            _usuarioPapelCondominioRepo.inserirPermissoesDoUsuarioAoCondominio(usuarioAntigo.getId(), idCondominio, pp.getId());
        }

        return new AtualizarUsuarioResponse(usuarioAntigo.getNome(), usuarioAntigo.getDocumento(),
                request.getPapeis());
    }

    public BuscarDadosDoPerfilResponse buscarDadosDeUmUsuario(Long idCondominio, String documentoUsuario)
    {
        Usuario usr = _usuarioService.buscarUsuarioPeloDocumento(documentoUsuario);
        if (usr == null)
        {
            throw new RuntimeException("Usuário não encontrado");
        }

        Collection<UsuarioPapelCondominio> link =
                _usuarioPapelCondominioRepo.buscarPorUsuarioECondominio(usr.getId(), idCondominio);

        Collection<String> papeis = new ArrayList<>();
        for (UsuarioPapelCondominio lk : link)
        {
            papeis.add(lk.getPapel().getNome());
        }

        Collection<CondominioResponse> cdres = new ArrayList<>();

        Optional<UsuarioPapelCondominio> condominioLink = link.stream().findFirst();

        UsuarioPapelCondominio llk = condominioLink.get();

        cdres.add(new CondominioResponse(llk.getCondominio().getId(), llk.getCondominio().getNome(),
                llk.getCondominio().getDocumento(), papeis));

        BuscarDadosDoPerfilResponse resp = new BuscarDadosDoPerfilResponse();

        resp.setId(usr.getId());
        resp.setNome(usr.getNome());
        resp.setDocumento(resp.getDocumento());
        resp.setCondominios(cdres);

        return resp;
    }

    // TODO: Usuários sem condomínios devem ser apagados
    public void removerUsuario(Long idCondominio, String documentoUsuario)
    {
        Usuario usr = _usuarioService.buscarUsuarioPeloDocumento(documentoUsuario);

        if (usr == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        _usuarioPapelCondominioRepo.apagarTodasPermissoesDoUsuarioAoCondominio(usr.getId(),
                 idCondominio);
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

    private Usuario mapearDadosParaNovoUsuario(CriarUsuarioRequest request,
                                               Papel papel)
    {
        Usuario usuario = new Usuario();

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setDocumento(request.getDocumento());
        usuario.setTelefone(request.getTelefone());

        usuario.getPapeis().add(papel);

        return usuario;
    }
}
