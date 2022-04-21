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
}
