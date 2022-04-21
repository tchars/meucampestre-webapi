package br.com.meucampestre.meucampestre.services;

import br.com.meucampestre.meucampestre.domain.models.Condominio;
import br.com.meucampestre.meucampestre.domain.models.Usuario;
import br.com.meucampestre.meucampestre.repositories.CondominioRepo;
import br.com.meucampestre.meucampestre.services.interfaces.ICondominioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CondominioService implements ICondominioService {

    private final CondominioRepo _condominioRepo;
    private final PasswordEncoder _passwordEncoder;

    @Override
    public Condominio salvarCondominio(Condominio condominio) {
        log.info("salvarCondominio - adicionando {}", condominio.getDocumento());

        condominio.setSenha(_passwordEncoder.encode(condominio.getSenha()));

        return _condominioRepo.save(condominio);
    }

    @Override
    public List<Condominio> buscarTodosCondominios()
    {
        log.info("todosCondominios.findAll");
        return _condominioRepo.findAll();
    }

    @Override
    public Condominio buscarCondominio(Long id)
    {
        log.info("buscarCondominio - buscando {}", id);
        return _condominioRepo
                .findById(id).get();
    }

    @Override
    public Collection<Usuario> buscarTodosUsuariosDeUmCondominio(Long idCondominio) {

        log.info("buscarTodosUsuariosDeUmCondominio - buscando {}", idCondominio);

        Condominio condominio = _condominioRepo.findById(idCondominio).get();

        Collection<Usuario> usuarios = new ArrayList<>();

        condominio.getUsuarios().stream().forEach(usuario -> {
            usuarios.add(usuario);
        });

        return usuarios;
    }
}
