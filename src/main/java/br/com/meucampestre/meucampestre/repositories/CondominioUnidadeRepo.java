package br.com.meucampestre.meucampestre.repositories;

import br.com.meucampestre.meucampestre.domain.models.CondominioUnidade;
import br.com.meucampestre.meucampestre.domain.models.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CondominioUnidadeRepo extends JpaRepository<CondominioUnidade, Long> {

    @Query(value = "SELECT * FROM condominio_unidade AS cu WHERE cu.condominio_id = ?1",
            nativeQuery = true)
    Optional<List<Unidade>> buscarTodasUnidadesDeUmCondominioPeloId(long idCondominio);
}
