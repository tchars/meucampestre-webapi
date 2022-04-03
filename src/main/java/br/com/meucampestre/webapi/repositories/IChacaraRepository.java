package br.com.meucampestre.webapi.repositories;

import br.com.meucampestre.webapi.entities.Chacara;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IChacaraRepository extends JpaRepository<Chacara, Long> {
}
