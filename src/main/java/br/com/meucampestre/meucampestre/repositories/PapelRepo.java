package br.com.meucampestre.meucampestre.repositories;

import br.com.meucampestre.meucampestre.domain.models.Papel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PapelRepo extends JpaRepository<Papel, Long> {
    Papel findByNome(String nome);
}
