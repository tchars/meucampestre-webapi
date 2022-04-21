package br.com.meucampestre.meucampestre.controllers;

import br.com.meucampestre.meucampestre.apimodels.condominio.BuscarTodosUsuariosDeUmCondominioResponse;
import br.com.meucampestre.meucampestre.apimodels.usuarios.*;
import br.com.meucampestre.meucampestre.apimodels.usuarios.partials.GenericResponse;
import br.com.meucampestre.meucampestre.applications.AutenticacaoApplication;
import br.com.meucampestre.meucampestre.applications.CondominioApplication;
import br.com.meucampestre.meucampestre.applications.UsuarioApplication;
import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import br.com.meucampestre.meucampestre.domain.constants.TiposDePapeis;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V1 + "/usuarios")
public class UsuarioController {

    private final UsuarioApplication _usuarioApplication;
    private final CondominioApplication _condominioApplication;
    private final AutenticacaoApplication _autenticacaoApplication;

    @PostMapping("/{idCondominio}/usuario")
    public ResponseEntity<?> salvarUsuario(@PathVariable Long idCondominio,
                                           @RequestBody CriarUsuarioRequest request)
    {
        try
        {
            String usuarioDoToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            _autenticacaoApplication.autenticarUsuario(usuarioDoToken, idCondominio,
                    TiposDePapeis.SINDICO);

            CriarUsuarioResponse usuarioCriado = _usuarioApplication
                                                    .criarUsuario(idCondominio,
                                                            request);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path(Rotas.URL_PREFIX_V1 + "/usuarios/" + idCondominio + "/usuario" +
                                    "/" + usuarioCriado.getId()
                            )
                            .toUriString());

            return ResponseEntity.created(uri).body(usuarioCriado);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(new GenericResponse(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage()));
        }
    }

    @GetMapping("/{idCondominio}/usuario")
    public ResponseEntity<?> buscarTodosUsuarioDeUmCondominio(@PathVariable Long idCondominio)
    {
        try
        {
            String usuarioDoToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            _autenticacaoApplication.autenticarUsuario(usuarioDoToken, idCondominio,
                    TiposDePapeis.SINDICO);

            BuscarTodosUsuariosDeUmCondominioResponse usuariosDeUmCondominio =
                    _condominioApplication.buscarTodosUsuariosDeUmCondominio(idCondominio);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path(Rotas.URL_PREFIX_V1 + "/usuarios/" + idCondominio + "/usuario" +
                                    "/" + usuariosDeUmCondominio.getId()
                            )
                            .toUriString());

            return ResponseEntity.created(uri).body(usuariosDeUmCondominio);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(new GenericResponse(HttpStatus.NOT_FOUND.value(),
                    e.getMessage()));
        }
    }

    @GetMapping("/{idCondominio}/usuario/{documentoUsuario}")
    public ResponseEntity<?> buscarUsuarioDeUmCondominio(@PathVariable Long idCondominio,
                                                         @PathVariable String documentoUsuario)
    {
        try
        {
            String usuarioDoToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            _autenticacaoApplication.autenticarUsuario(usuarioDoToken, idCondominio,
                    TiposDePapeis.SINDICO);

            BuscarDadosDoPerfilResponse usuario =
                    _usuarioApplication.buscarDadosDeUmUsuario(idCondominio, documentoUsuario);

            return ResponseEntity.ok().body(usuario);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(new GenericResponse(HttpStatus.NOT_FOUND.value(),
                    e.getMessage()));
        }
    }


    @PutMapping("/{idCondominio}/usuario")
    public ResponseEntity<AtualizarUsuarioResponse> atualizarPerfil(@PathVariable Long idCondominio,
                                                                    @RequestBody
                                                                            AtualizarUsuarioRequest request)
    {
        String usuarioDoToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        _autenticacaoApplication.autenticarUsuario(usuarioDoToken, idCondominio,
                TiposDePapeis.SINDICO);

        AtualizarUsuarioResponse usuarioAtualizado =
                _usuarioApplication.atualizarPerfilDoUsuario(idCondominio, request);

        if (usuarioAtualizado == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(usuarioAtualizado);
    }

    @DeleteMapping("/{idCondominio}/usuario/{documentoUsuario}")
    public ResponseEntity<?> removerUsuario(@PathVariable Long idCondominio,
                                            @PathVariable String documentoUsuario)
    {
        try {
            String usuarioDoToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            _autenticacaoApplication.autenticarUsuario(usuarioDoToken, idCondominio,
                    TiposDePapeis.SINDICO);


            _usuarioApplication.removerUsuario(idCondominio, documentoUsuario);

            return ResponseEntity.accepted().build();

        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().body(new GenericResponse(HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage()));
        }
    }
}
