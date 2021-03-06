package br.com.meucampestre.meucampestre.controllers.backoffice;

import br.com.meucampestre.meucampestre.apimodels.condominio.BuscarTodosUsuariosDeUmCondominioResponse;
import br.com.meucampestre.meucampestre.apimodels.condominio.CriarCondominioRequest;
import br.com.meucampestre.meucampestre.apimodels.condominio.CriarCondominioResponse;
import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioRequest;
import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioResponse;
import br.com.meucampestre.meucampestre.apimodels.usuarios.partials.GenericResponse;
import br.com.meucampestre.meucampestre.applications.CondominioApplication;
import br.com.meucampestre.meucampestre.applications.UsuarioApplication;
import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import br.com.meucampestre.meucampestre.domain.models.Condominio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V1 + "/backoffice")
public class BackofficeController {

    private final CondominioApplication _condominioApplication;
    private final UsuarioApplication _usuarioApplication;

    // CONTEXTO: CONDOMINIO
    @GetMapping("/condominios")
    public ResponseEntity<List<Condominio>> buscarTodosCondominios() {
        return ResponseEntity.ok().body(_condominioApplication.buscarTodosCondominios());
    }

    @GetMapping("/condominios/{idCondominio}")
    public ResponseEntity<Condominio> buscarCondominio(@PathVariable Long idCondominio) {
        return ResponseEntity.ok().body(_condominioApplication.buscarCondominio(idCondominio));
    }

    @PostMapping("/condominios")
    public ResponseEntity<CriarCondominioResponse> salvarCondominio(@RequestBody CriarCondominioRequest request)
    {
        CriarCondominioResponse condominioCriado = _condominioApplication.criarCondominio(request);

        URI uri = URI.create(
                ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(Rotas.URL_PREFIX_V1 + "/condominios/" + condominioCriado.getId()
                )
        .toUriString());

        return ResponseEntity.created(uri).body(condominioCriado);
    }

    // Contexto: USUARIO
    @GetMapping("/condominios/{idCondominio}/usuarios")
    public ResponseEntity<BuscarTodosUsuariosDeUmCondominioResponse> buscarTodosUsuarios(@PathVariable Long idCondominio)
    {
        return ResponseEntity.ok().body(_condominioApplication.buscarTodosUsuariosDeUmCondominio(idCondominio));
    }

    @PostMapping("/condominios/{idCondominio}/usuarios")
    public ResponseEntity<?> salvarUsuario(@PathVariable Long idCondominio,
                                                              @RequestBody CriarUsuarioRequest request)
    {
        try
        {
            CriarUsuarioResponse usuarioCriado = _usuarioApplication.criarUsuario(idCondominio,
                    request);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path(Rotas.URL_PREFIX_V1 + "/condominios/usuarios/" + usuarioCriado.getId()
                            )
                            .toUriString());

            return ResponseEntity.created(uri).body(usuarioCriado);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(
                    new GenericResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage())
            );
        }
    }
}
