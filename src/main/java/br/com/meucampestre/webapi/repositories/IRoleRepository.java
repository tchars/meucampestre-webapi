package br.com.meucampestre.webapi.repositories;

import br.com.meucampestre.webapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNome(String nome);
}
