package br.com.meucampestre.webapi.services;

import br.com.meucampestre.webapi.dto.usuario.UsuarioDTO;
import br.com.meucampestre.webapi.entities.Condominio;
import br.com.meucampestre.webapi.entities.Role;
import br.com.meucampestre.webapi.entities.Usuario;
import br.com.meucampestre.webapi.exceptions.DocumentoJaUtilizadoException;
import br.com.meucampestre.webapi.exceptions.EmailJaUtilizadoException;
import br.com.meucampestre.webapi.exceptions.RecursoNaoEncontradoException;
import br.com.meucampestre.webapi.repositories.IRoleRepository;
import br.com.meucampestre.webapi.repositories.IUsuarioRepository;
import br.com.meucampestre.webapi.services.interfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "userService")
public class UsuarioService implements IUsuarioService, UserDetailsService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper _modelMapper;

    @Autowired
    private IUsuarioRepository userDao;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> findByEmail = userDao.findByEmail(username);
        if(findByEmail.isEmpty()){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Usuario user = findByEmail.get();
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getSenha(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(Usuario user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getNome()));
        });
        return authorities;
    }

    public List<Usuario> findAll() {
        List<Usuario> list = new ArrayList<>();
        userDao.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    public Usuario findOne(String username) {
        return userDao.findByEmail(username).get();
    }

    @Override
    public Usuario save(UsuarioDTO user) {

        Usuario nUser = _modelMapper.map(user, Usuario.class);
        nUser.setSenha(bcryptEncoder.encode(user.getSenha()));

        Role role = roleService.findByName("ROLE_SINDICO");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        if(nUser.getEmail().split("@")[1].equals("admin.com")){
            role = roleService.findByName("ROLE_ADMIN");
            roleSet.add(role);
        }

        nUser.setRoles(roleSet);
        return userDao.save(nUser);
    }

    @Override
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        return null;
    }

    @Override
    public Usuario buscarUsuarioPorEmail(String email) {
        return null;
    }
}
