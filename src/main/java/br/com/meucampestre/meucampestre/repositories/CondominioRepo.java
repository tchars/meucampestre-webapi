package br.com.meucampestre.meucampestre.repositories;

import br.com.meucampestre.meucampestre.domain.models.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CondominioRepo extends JpaRepository<Condominio, Long> {
}
