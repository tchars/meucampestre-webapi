package br.com.meucampestre.webapi.repositories;

import br.com.meucampestre.webapi.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByDocumento(String documento);

    Boolean existsByDocumento(String documento);

    Boolean existsByEmail(String email);
}
