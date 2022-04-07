package br.com.meucampestre.webapi.services;

import br.com.meucampestre.webapi.dto.autenticacao.CriarNovoUsuarioDTO;
import br.com.meucampestre.webapi.dto.autenticacao.LoginDTO;
import br.com.meucampestre.webapi.dto.autenticacao.JWTAuthResponseDTO;
import br.com.meucampestre.webapi.dto.condominio.CondominioDTO;
import br.com.meucampestre.webapi.dto.usuario.UsuarioDTO;
import br.com.meucampestre.webapi.entities.Role;
import br.com.meucampestre.webapi.entities.Usuario;
import br.com.meucampestre.webapi.exceptions.DocumentoJaUtilizadoException;
import br.com.meucampestre.webapi.exceptions.EmailJaUtilizadoException;
import br.com.meucampestre.webapi.repositories.IRoleRepository;
import br.com.meucampestre.webapi.repositories.IUsuarioRepository;
import br.com.meucampestre.webapi.security.JWTTokenProvider;
import br.com.meucampestre.webapi.services.interfaces.IAutenticacaoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AutenticacaoService implements IAutenticacaoService {

    private final AuthenticationManager _authenticationManager;
    private final JWTTokenProvider _jwtTokenProvider;
    private final IUsuarioRepository _usuarioRepository;
    private final IRoleRepository _roleRepository;
    private final PasswordEncoder _passwordEncoder;
    private final ModelMapper _modelMapper;

    @Autowired
    public AutenticacaoService(AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider, IUsuarioRepository usuarioRepository, IRoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        _authenticationManager = authenticationManager;
        _jwtTokenProvider = jwtTokenProvider;
        _usuarioRepository = usuarioRepository;
        _roleRepository = roleRepository;
        _passwordEncoder = passwordEncoder;
        _modelMapper = modelMapper;
    }

    @Override
    public JWTAuthResponseDTO autenticarUsuario(LoginDTO usuario)
    {
        try
        {
            Authentication authentication = _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = _jwtTokenProvider.gerarToken(authentication);

            return new JWTAuthResponseDTO(token);
        }
        catch (Exception exception)
        {
            throw exception;
        }
    }

    @Override
    public CriarNovoUsuarioDTO registrarNovoUsuario(UsuarioDTO registroDTO) {

        if (_usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new EmailJaUtilizadoException("Email", registroDTO.getEmail());
        }

        if (_usuarioRepository.existsByDocumento(registroDTO.getDocumento())) {
            throw new DocumentoJaUtilizadoException("Documento", registroDTO.getDocumento());
        }

        Usuario usuario = _modelMapper.map(registroDTO, Usuario.class);

        usuario.setSenha(_passwordEncoder.encode(registroDTO.getSenha()));

        Role roles = _roleRepository.findByNome("ROLE_SINDICO").get();

        usuario.setRoles(Collections.singleton(roles));

        Usuario usuarioCriado = _usuarioRepository.save(usuario);

        return _modelMapper.map(usuarioCriado, CriarNovoUsuarioDTO.class);
    }
}
