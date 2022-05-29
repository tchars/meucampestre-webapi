package br.com.meucampestre.meucampestre.applications;

import br.com.meucampestre.meucampestre.apimodels.condominio.BuscarTodosUsuariosDeUmCondominioResponse;
import br.com.meucampestre.meucampestre.apimodels.condominio.CriarCondominioRequest;
import br.com.meucampestre.meucampestre.apimodels.condominio.CriarCondominioResponse;
import br.com.meucampestre.meucampestre.apimodels.condominio.partials.MoradorDoCondominio;
import br.com.meucampestre.meucampestre.apimodels.usuarios.partials.CondominioResponse;
import br.com.meucampestre.meucampestre.domain.constants.TiposDePapeis;
import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.UsuarioPapelCondominio;
import br.com.meucampestre.meucampestre.repositories.CondominioRepo;
import br.com.meucampestre.meucampestre.repositories.UsuarioPapelCondominioRepo;
import br.com.meucampestre.meucampestre.services.interfaces.ICondominioService;
import br.com.meucampestre.meucampestre.services.interfaces.IPapelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CondominioApplication {

    private final ICondominioService _condominioService;
    private final CondominioRepo _condominioRepo;
    private final UsuarioPapelCondominioRepo _usuarioPapelCondominioRepo;

    // Contexto - CONDOMÍNIO
    public CriarCondominioResponse criarCondominio(CriarCondominioRequest request)
    {
        Condominio condominio = _condominioRepo.getCondominioPorDocumento(request.getDocumento()).get();
        if (condominio != null)
        {
            throw new RuntimeException("Condominínio já cadastrado");
        }

        Condominio condominioASerCadastrado = new Condominio();

        condominioASerCadastrado.setNome(request.getNome());
        condominioASerCadastrado.setDescricao(request.getDescricao());
        condominioASerCadastrado.setEmail(request.getEmail());
        condominioASerCadastrado.setDocumento(request.getDocumento());
        condominioASerCadastrado.setEndereco(request.getEndereco());
        condominioASerCadastrado.setImagemUrl(request.getImagemUrl());

        _condominioService.salvarCondominio(condominioASerCadastrado);

        CriarCondominioResponse resp = new CriarCondominioResponse();

        resp.setNome(condominioASerCadastrado.getNome());
        resp.setDescricao(condominioASerCadastrado.getDescricao());
        resp.setEmail(condominioASerCadastrado.getEmail());
        resp.setDocumento(condominioASerCadastrado.getDocumento());
        resp.setImagemUrl(condominioASerCadastrado.getImagemUrl());

        return resp;
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
       Collection<UsuarioPapelCondominio> condominio =
               _usuarioPapelCondominioRepo.buscarTodosUsuariosDeUmCondominio(idCondominio);

       Condominio cond = _condominioService.buscarCondominio(idCondominio);

       BuscarTodosUsuariosDeUmCondominioResponse response =
               new BuscarTodosUsuariosDeUmCondominioResponse();

       for (UsuarioPapelCondominio usr : condominio)
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