package br.com.meucampestre.meucampestre.applications;

import br.com.meucampestre.meucampestre.apimodels.condominio.BuscarTodosUsuariosDeUmCondominioResponse;
import br.com.meucampestre.meucampestre.apimodels.condominio.CriarCondominioRequest;
import br.com.meucampestre.meucampestre.apimodels.condominio.CriarCondominioResponse;
import br.com.meucampestre.meucampestre.apimodels.condominio.partials.MoradorDoCondominio;
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
import org.yaml.snakeyaml.util.ArrayUtils;

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
    public BuscarTodosUsuariosDeUmCondominioResponse buscarTodosUsuariosDeUmCondominio(Long idCondominio)
    {
       Collection<UsuarioPapelCondominioLink> condominio =
               _usuarioPapelCondominioLinkRepo.buscarTodosUsuariosDeUmCondominio(idCondominio);

       Condominio cond = _condominioService.buscarCondominio(idCondominio);

       BuscarTodosUsuariosDeUmCondominioResponse response =
               new BuscarTodosUsuariosDeUmCondominioResponse();

       for (UsuarioPapelCondominioLink usr : condominio)
       {
           if (response.getMoradores().size() < 1)
           {
               Collection<String> papeisConcat = new ArrayList<>();
               papeisConcat.add(usr.getPapel().getNome());

               response.getMoradores().add(
                       new MoradorDoCondominio(
                               usr.getUsuario().getId(),
                               usr.getUsuario().getNome(),
                               usr.getUsuario().getDocumento(),
                               papeisConcat,
                               null
                       )
               );
           }
           else
           {
               boolean devoAdicionar = true;

               for (MoradorDoCondominio morador : response.getMoradores())
               {
                   if (morador.getDocumento().equals(usr.getUsuario().getDocumento()))
                   {
                       morador.getTipoDePerfil().add(usr.getPapel().getNome());
                       devoAdicionar = false;
                       break;
                   }
               }

               if (devoAdicionar)
               {
                   Collection<String> papeisConcat = new ArrayList<>();
                   papeisConcat.add(usr.getPapel().getNome());

                   response.getMoradores().add(
                       new MoradorDoCondominio(
                           usr.getUsuario().getId(),
                           usr.getUsuario().getNome(),
                           usr.getUsuario().getDocumento(),
                           papeisConcat,
                           null
                       )
                   );
               }
           }
       }

       response.setId(cond.getId());
       response.setNome(cond.getNome());
       response.setDocumento(cond.getDocumento());

       return response;
    }
}