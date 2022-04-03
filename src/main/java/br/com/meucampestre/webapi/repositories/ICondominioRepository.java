package br.com.meucampestre.webapi.repositories;

import br.com.meucampestre.webapi.entities.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICondominioRepository extends JpaRepository<Condominio, Long> {
}
