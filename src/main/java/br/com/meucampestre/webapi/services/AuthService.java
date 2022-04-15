package br.com.meucampestre.webapi.services;

import br.com.meucampestre.webapi.dto.autenticacao.LoginDTO;
import br.com.meucampestre.webapi.dto.autenticacao.v1.TokenDTO;
import br.com.meucampestre.webapi.dto.autenticacao.v1.ValidateTokenDTO;
import br.com.meucampestre.webapi.entities.Usuario;
import br.com.meucampestre.webapi.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final IUsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    @Autowired
    public AuthService(PasswordEncoder encoder,IUsuarioRepository repository, JwtService jwtService) {
        this.encoder = encoder;
        this.repository = repository;
        this.jwtService = jwtService;
    }

    public TokenDTO authenticate(LoginDTO loginDTO) {

        Usuario user = new Usuario();
        user.setEmail(loginDTO.getEmail());
        user.setSenha(loginDTO.getSenha());

        authenticate(user);

        String token = jwtService.generateToken(user);

        return new TokenDTO(user.getEmail(), token);
    }

    public UserDetails authenticate(Usuario user) {
        UserDetails userDetails = loadUserByUsername(user.getEmail());

        boolean passwordMatches = encoder.matches(user.getSenha(), userDetails.getPassword());

        if (!userDetails.isEnabled()) {
            // TODO: ADICIONAR EXCEPTION
            //throw new UserEnableException();
            throw new RuntimeException("Usuario UserEnableException");
        }

        if (!passwordMatches) {
            //throw new InvalidPasswordException();
            // TODO: ADICIONAR EXCEPTION
            throw new RuntimeException("Usuario InvalidPasswordException");
        }

        return userDetails;
    }

    public ValidateTokenDTO validateToken(ValidateTokenDTO validateTokenDTO) {
        boolean isValid = jwtService.validateToken(validateTokenDTO.getToken());

        if (!isValid) {
            //throw new InvalidTokenException();
            throw new RuntimeException("Usuario InvalidTokenException");
        }

        return validateTokenDTO;
    }




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Usuario user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getSenha())
                .roles(null)
                // TODO: ADICIONAR EXCLUSÃO LÓGICA
                //.disabled(!user.isEnabled())
                .disabled(false)
                .build();
    }
}
