package br.com.meucampestre.meucampestre.services;

import br.com.meucampestre.meucampestre.apimodels.condominio.partials.MoradorDoCondominio;
import br.com.meucampestre.meucampestre.apimodels.usuarios.BuscarDadosDoPerfilResponse;
import br.com.meucampestre.meucampestre.apimodels.usuarios.partials.CondominioResponse;
import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.domain.models.Papel;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.domain.models.UsuarioPapelCondominioLink;
import br.com.meucampestre.meucampestre.repositories.CondominioRepo;
import br.com.meucampestre.meucampestre.repositories.PapelRepo;
import br.com.meucampestre.meucampestre.repositories.UsuarioPapelCondominioLinkRepo;
import br.com.meucampestre.meucampestre.repositories.UsuarioRepo;
import br.com.meucampestre.meucampestre.services.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UsuarioService implements IUsuarioService, UserDetailsService {

    private final UsuarioRepo _usuarioRepo;
    private final PapelRepo _papelRepo;
    private final CondominioRepo _condominioRepo;
    private final UsuarioPapelCondominioLinkRepo _usuarioPapelCondominioLinkRepo;
    private final PasswordEncoder _passwordEncoder;

    @Override
    public BuscarDadosDoPerfilResponse buscarDadosDoUsuarioPeloToken(String documento)
    {
        Usuario usuario = _usuarioRepo.findByDocumento(documento);

        Collection<UsuarioPapelCondominioLink> link =
                _usuarioPapelCondominioLinkRepo.buscarPorUsuario(usuario.getId());

        BuscarDadosDoPerfilResponse response = new BuscarDadosDoPerfilResponse();

        for (UsuarioPapelCondominioLink item : link)
        {
            Collection<String> pp = new ArrayList<>();

            if (response.getCondominios().size() < 1)
            {
                pp.add(item.getPapel().getNome());

                response.getCondominios().add(new CondominioResponse(
                        item.getId(),
                        item.getCondominio().getNome(),
                        item.getCondominio().getDocumento(),
                        pp));
            }
            else
            {
                boolean devoAdicionar = true;

                for (CondominioResponse condominio : response.getCondominios())
                {
                    if (condominio.getDocumento().equals(item.getCondominio().getDocumento()))
                    {
                        condominio.getTipoDePerfil().add(item.getPapel().getNome());
                        devoAdicionar = false;
                        break;
                    }
                }

                if (devoAdicionar)
                {
                    Collection<String> papeisConcat = new ArrayList<>();
                    papeisConcat.add(item.getPapel().getNome());

                    response.getCondominios().add(
                        new CondominioResponse(
                            item.getCondominio().getId(),
                            item.getCondominio().getNome(),
                            item.getCondominio().getDocumento(),
                            papeisConcat
                        )
                    );
                }
            }
        }

        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setDocumento(usuario.getDocumento());

        return response;
    }






    // Sem defini????o
    @Override
    public Usuario buscarUsuarioPeloDocumento(String documento) {
        return _usuarioRepo.findByDocumento(documento);
    }

    @Override
    public List<Usuario> buscarUsuarios() {
        return _usuarioRepo.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = _usuarioRepo.findByDocumento(username);

        if (usuario == null) {
            log.error("Usu??rio n??o encontrado");
            throw new UsernameNotFoundException("Usu??rio n??o encontrado");
        }
        else {
            log.info("Usu??rio: {} encontrado", username);
        }

        Collection<SimpleGrantedAuthority> authoroties = new ArrayList<>();

        usuario.getPapeis().forEach(role -> {
            authoroties.add(new SimpleGrantedAuthority(role.getNome()));
        });

        return new org.springframework.security.core.userdetails.User(usuario.getDocumento(),
                usuario.getSenha(), authoroties);
    }

    @Override
    public Usuario salvarUsuario(Usuario usuario) {
        log.info("salvarUsuario - adicionando {}", usuario.getDocumento());

        usuario.setSenha(_passwordEncoder.encode(usuario.getSenha()));

        return _usuarioRepo.save(usuario);
    }

    public Usuario atualizarUsuario(Usuario usuario) {
        log.info("salvarUsuario - adicionando {}", usuario.getDocumento());

        return _usuarioRepo.save(usuario);
    }

    @Override
    public Papel salvarPapel(Papel papel) {
        log.info("salvarPapel - adicionando {}", papel.getNome());
        return _papelRepo.save(papel);
    }

    @Override
    public void adicionarPapelAoUsuario(String documento, String nomePapel) {

        log.info("adicionarPapelAoUsuario - adicionando {} ao documento {}", nomePapel, documento);

        Usuario usuario = _usuarioRepo.findByDocumento(documento);
        Papel papel = _papelRepo.findByNome(nomePapel);
        usuario.getPapeis().add(papel);
    }
}
