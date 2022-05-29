package br.com.meucampestre.meucampestre.repositories;

import br.com.meucampestre.meucampestre.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByDocumento(String documento);
    Optional<Usuario> getById(long id);
}
