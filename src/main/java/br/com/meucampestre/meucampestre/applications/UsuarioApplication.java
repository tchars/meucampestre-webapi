package br.com.meucampestre.meucampestre.applications;

import br.com.meucampestre.meucampestre.apimodels.usuarios.*;
import br.com.meucampestre.meucampestre.apimodels.usuarios.partials.CondominioResponse;
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
public class UsuarioApplication {

    private final IUsuarioService _usuarioService;
    private final IPapelService _papelService;
    private final ICondominioService _condominioService;
    private final PasswordEncoder _encoder;
    private final UsuarioPapelCondominioLinkRepo _usuarioPapelCondominioLinkRepo;

    public CriarUsuarioResponse criarUsuario(Long idCondominio,
                                             CriarUsuarioRequest request)
    {
        Usuario usuarioASerCriado =
                _usuarioService.buscarUsuarioPeloDocumento(request.getDocumento());
        Papel papelDesejado = _papelService.buscarPapelPeloNome(request.getPapel());
        Condominio condominio = _condominioService.buscarCondominio(idCondominio);

        if (papelDesejado == null)
        {
            throw new RuntimeException("Permissão/Role não encontrada");
        }

        if (condominio == null) {
            throw new RuntimeException("Condomínio não encontrado");
        }

        // Usuário não existe no sistema
        if (usuarioASerCriado == null)
        {
            // Cadastro
            usuarioASerCriado = _usuarioService.salvarUsuario(
                    mapearDadosParaNovoUsuario(request, papelDesejado));

            _usuarioPapelCondominioLinkRepo.save(new UsuarioPapelCondominioLink(null,
                    usuarioASerCriado, condominio, papelDesejado));

            return mapParaResponse(usuarioASerCriado, condominio);
        }

        // Se o usuário já existe no sistema
        // verifico se ele já existe no condomínio
        UsuarioPapelCondominioLink usuarioJaExisteComARoleNesteCondominio =
                _usuarioPapelCondominioLinkRepo
                        .buscarPorUsuarioCondominioPapel(usuarioASerCriado.getId(), condominio.getId(),
                                papelDesejado.getId());

        // Se ele não tiver relação entre usuário - condomínio - role pedida, posso criar
        if (usuarioJaExisteComARoleNesteCondominio == null)
        {
            UsuarioPapelCondominioLink usuario =
                    _usuarioPapelCondominioLinkRepo.save(new UsuarioPapelCondominioLink(null,
                    usuarioASerCriado, condominio, papelDesejado));

            return mapParaResponse(usuarioASerCriado, condominio);
        }

        throw new RuntimeException("Usuário já possui cadastro neste condomínio com esta role");
    }

    public AtualizarUsuarioResponse atualizarPerfilDoUsuario(Long idCondominio,
                                                             AtualizarUsuarioRequest request)
    {
        Usuario usuarioAntigo = _usuarioService.buscarUsuarioPeloDocumento(request.getDocumento());

        if (usuarioAntigo == null) {
            throw new RuntimeException("Usuário não existe");
        }

        Collection<Papel> papeis = new ArrayList<>();
        for (String papel : request.getPapeis())
        {
            papeis.add(_papelService.buscarPapelPeloNome(papel));
        }

        //private String nome;
        usuarioAntigo.setNome(request.getNome());
        //private String documento;
        usuarioAntigo.setDocumento(request.getDocumento());

        //private Collection<String> papeis = new ArrayList<>();
        usuarioAntigo.setPapeis(papeis);

        _usuarioService.salvarUsuario(usuarioAntigo);

        _usuarioPapelCondominioLinkRepo.apagarTodasPermissoesDoUsuarioAoCondominio(usuarioAntigo.getId(), idCondominio);

        for (Papel pp : papeis)
        {
            _usuarioPapelCondominioLinkRepo.inserirPermissoesDoUsuarioAoCondominio(usuarioAntigo.getId(), idCondominio, pp.getId());
        }

        return new AtualizarUsuarioResponse(usuarioAntigo.getNome(), usuarioAntigo.getDocumento(),
                request.getPapeis());
    }

    public BuscarDadosDoPerfilResponse buscarDadosDeUmUsuario(Long idCondominio, String documentoUsuario)
    {
        Usuario usr = _usuarioService.buscarUsuarioPeloDocumento(documentoUsuario);

        Collection<UsuarioPapelCondominioLink> link =
                _usuarioPapelCondominioLinkRepo.buscarPorUsuarioECondominio(usr.getId(), idCondominio);

        Collection<String> papeis = new ArrayList<>();
        for (UsuarioPapelCondominioLink lk : link)
        {
            papeis.add(lk.getPapel().getNome());
        }

        Collection<CondominioResponse> cdres = new ArrayList<>();

        Optional<UsuarioPapelCondominioLink> condominioLink = link.stream().findFirst();

        UsuarioPapelCondominioLink llk = condominioLink.get();

        cdres.add(new CondominioResponse(llk.getCondominio().getId(), llk.getCondominio().getNome(),
                llk.getCondominio().getDocumento(), papeis));

        return new BuscarDadosDoPerfilResponse(usr.getId(), usr.getNome(), usr.getDocumento(),
                cdres);
    }

    // TODO: Usuários sem condomínios devem ser apagados
    public void removerUsuario(Long idCondominio, String documentoUsuario)
    {
        Usuario usr = _usuarioService.buscarUsuarioPeloDocumento(documentoUsuario);

        if (usr == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        _usuarioPapelCondominioLinkRepo.apagarTodasPermissoesDoUsuarioAoCondominio(usr.getId(),
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
