package br.com.meucampestre.meucampestre.controllers;

import br.com.meucampestre.meucampestre.apimodels.usuarios.AtualizarUsuarioRequest;
import br.com.meucampestre.meucampestre.apimodels.usuarios.AtualizarUsuarioResponse;
import br.com.meucampestre.meucampestre.apimodels.usuarios.BuscarDadosDoPerfilResponse;
import br.com.meucampestre.meucampestre.domain.constants.Rotas;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Rotas.URL_PREFIX_V1 + "/minhaConta")
public class MinhaContaController {

    private final UsuarioService _usuarioService;

    // Buscar todos os dados do próprio usuário
    @GetMapping()
    public ResponseEntity<BuscarDadosDoPerfilResponse> buscarDadosDoUsuarioAPartirDoToken()
    {
        String usuarioDoToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok().body(_usuarioService.buscarDadosDoUsuarioPeloToken(usuarioDoToken));
    }

    @PutMapping
    public ResponseEntity<AtualizarUsuarioResponse> atualizarOProprioPerfil() {

        String usuarioDoToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return null;
    }
}