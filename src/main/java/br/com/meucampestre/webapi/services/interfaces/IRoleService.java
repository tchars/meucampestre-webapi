package br.com.meucampestre.webapi.services.interfaces;

import br.com.meucampestre.webapi.entities.Role;
import br.com.meucampestre.webapi.entities.Usuario;

public interface IRoleService {

    Role findByName(String name);

}
