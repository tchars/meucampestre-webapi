package br.com.meucampestre.webapi.security;

import br.com.meucampestre.webapi.entities.Role;
import br.com.meucampestre.webapi.entities.Usuario;
import br.com.meucampestre.webapi.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUsuarioRepository _usuarioRepository;

    @Autowired
    public CustomUserDetailsService(IUsuarioRepository usuarioRepository) {
        _usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailUsuario) throws UsernameNotFoundException
    {
        Usuario usuario = _usuarioRepository.findByEmail(emailUsuario)
                                            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado - " + emailUsuario));

        return new User(usuario.getEmail(), usuario.getSenha(), mapearRoles(usuario.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapearRoles(Set<Role> roles)
    {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNome())).collect(Collectors.toList());
    }
}
