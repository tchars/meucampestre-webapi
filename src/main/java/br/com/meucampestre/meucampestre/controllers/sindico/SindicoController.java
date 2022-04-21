package br.com.meucampestre.meucampestre.controllers.sindico;

import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioRequest;
import br.com.meucampestre.meucampestre.apimodels.usuarios.CriarUsuarioResponse;
import br.com.meucampestre.meucampestre.apimodels.usuarios.partials.GenericResponse;
import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V1 + "/sindico")
public class SindicoController {

//    @PostMapping("/condominios/{idCondominio}/usuarios")
//    public ResponseEntity<?> salvarUsuario(@PathVariable Long idCondominio,
//                                           @RequestBody CriarUsuarioRequest request)
//    {
//        try {
//            CriarUsuarioResponse usuarioCriado = _condominioApplication.criarUsuario(idCondominio,
//                    request);
//
//            URI uri = URI.create(
//                    ServletUriComponentsBuilder
//                            .fromCurrentContextPath()
//                            .path(Rotas.URL_PREFIX_V1 + "/condominios/usuarios/" + usuarioCriado.getId()
//                            )
//                            .toUriString());
//
//            return ResponseEntity.created(uri).body(usuarioCriado);
//
//        }
//
//        catch (Exception e) {
//            return ResponseEntity.badRequest().body(new GenericResponse(HttpStatus.BAD_REQUEST.value(),
//                    e.getMessage()));
//        }
//    }

}
