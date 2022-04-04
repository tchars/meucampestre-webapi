package br.com.meucampestre.webapi.controllers;

import br.com.meucampestre.webapi.dto.usuario.LoginDTO;
import br.com.meucampestre.webapi.dto.usuario.UsuarioDTO;
import br.com.meucampestre.webapi.entities.Role;
import br.com.meucampestre.webapi.entities.Usuario;
import br.com.meucampestre.webapi.repositories.IRoleRepository;
import br.com.meucampestre.webapi.repositories.IUsuarioRepository;
import br.com.meucampestre.webapi.security.JWTAuthResponseDTO;
import br.com.meucampestre.webapi.security.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager _authenticationManager;
    private final IUsuarioRepository _usuarioRepository;
    private final IRoleRepository _roleRepository;
    private final PasswordEncoder _passwordEncoder;
    private final JWTTokenProvider _jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, IUsuarioRepository usuarioRepository, IRoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTTokenProvider jwtTokenProvider) {
        _authenticationManager = authenticationManager;
        _usuarioRepository = usuarioRepository;
        _roleRepository = roleRepository;
        _passwordEncoder = passwordEncoder;
        _jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/iniciarSessao")
    public ResponseEntity<JWTAuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO)
    {
        Authentication authentication = _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Geramos o token
        String token = _jwtTokenProvider.gerarToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioDTO registroDTO) {

        if (_usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            return new ResponseEntity<>("Erro - Email já utilizado", HttpStatus.BAD_REQUEST);
        }

        if(_usuarioRepository.existsByDocumento(registroDTO.getDocumento())) {
            return new ResponseEntity<>("Erro - Documento já utilizado",HttpStatus.BAD_REQUEST);
        }

        Usuario usuario = new Usuario();

        usuario.setNome(registroDTO.getNome());
        usuario.setDocumento(registroDTO.getDocumento());
        usuario.setEmail(registroDTO.getEmail());

        usuario.setSenha(_passwordEncoder.encode(registroDTO.getSenha()));

        Role roles = _roleRepository.findByNome("ROLE_ADMIN").get();

        usuario.setRoles(Collections.singleton(roles));

        _usuarioRepository.save(usuario);

        return new ResponseEntity<>("Usuário registrado com sucesso!", HttpStatus.OK);
    }
}
