package br.com.meucampestre.meucampestre.applications;

import br.com.meucampestre.meucampestre.apimodels.condominio.CriarCondominioRequest;
import br.com.meucampestre.meucampestre.apimodels.condominio.CriarCondominioResponse;
import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioRequest;
import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioResponse;
import br.com.meucampestre.meucampestre.domain.constants.TiposDePapeis;
import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
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
        Papel papel = _papelService.buscarPapelPeloNome(request.getPapel());
        Condominio condominio = _condominioService.buscarCondominio(idCondominio);

        Boolean usuarioExisteNesteCondominio = false;

        if (papel == null) {
            throw new RuntimeException("Permissão/Role não encontrada");
        }

        if (condominio == null) {
            throw new RuntimeException("Condomínio não encontrado");
        }

        if (usuario != null)
        {
            for (Usuario usuarioItem : condominio.getUsuarios())
            {
                // Usuário existe neste condomínio?
                if (usuarioItem.getDocumento().equals(request.getDocumento()))
                {
                    // usuario Ja Possui Este Papel?
                    for (Papel papelItem : usuarioItem.getPapeis())
                    {
                        if (papelItem.getNome().equals(papel.getNome()))
                        {
                            throw new RuntimeException("Usuário já existe neste condominio com esta role");
                        }
                    }

                    usuarioExisteNesteCondominio = true;

                    break;
                }
            }
        }

        // Salvando o usuário
        if (usuarioExisteNesteCondominio)
        {
            // Se ele já existe neste condomínio, então apenas adiciono uma role
            usuario.getPapeis().add(papel);
        }
        else
        {
            usuario = new Usuario(null, request.getNome(), request.getSenha(),
                    request.getDocumento(), new ArrayList<>(), new Date(System.currentTimeMillis()),
                    new Date(System.currentTimeMillis()));

            usuario.getPapeis().add(papel);
        }

        Usuario usuarioCriado;
        if (usuarioExisteNesteCondominio)
        {
            // Se ele já existir não preciso recriptografar a senha
            usuarioCriado = _usuarioService.atualizarUsuario(usuario);
        }
        else
        {
            usuarioCriado = _usuarioService.salvarUsuario(usuario);
        }

        // Atribuindo ele a um condomínio se ele não tiver um
        if (!usuarioExisteNesteCondominio)
        {
            condominio = _condominioService.buscarCondominio(idCondominio);
            condominio.getUsuarios().add(usuarioCriado);
            _condominioService.salvarCondominio(condominio);
        }

        return new CriarUsuarioResponse(usuarioCriado.getId(),
                usuarioCriado.getNome(),
                usuarioCriado.getDocumento(),
                usuarioCriado.getPapeis().stream().collect(Collectors.toList()),
                condominio.getId()
        );
    }
}
