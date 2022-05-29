package br.com.meucampestre.meucampestre.repositories;

import br.com.meucampestre.meucampestre.domain.models.Papel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PapelRepo extends JpaRepository<Papel, Long> {
    Papel findByNome(String nome);

    Optional<Papel> getByNome(String nome);
}
