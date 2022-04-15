package br.com.meucampestre.webapi.services;

import br.com.meucampestre.webapi.entities.Role;
import br.com.meucampestre.webapi.repositories.IRoleRepository;
import br.com.meucampestre.webapi.services.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleDao;

    @Override
    public Role findByName(String name) {
        Optional<Role> role = roleDao.findByNome(name);
        return role.get();
    }
}
