package br.com.meucampestre.meucampestre.repositories;

import br.com.meucampestre.meucampestre.domain.models.UnidadeUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UnidadeUsuarioRepo extends JpaRepository<UnidadeUsuario, Long> {

    @Query(value = "SELECT * FROM unidade_usuario AS udu " +
            "WHERE udu.unidade_id = ?1 AND udu.usuario_id = ?2", nativeQuery = true)
    Optional<UnidadeUsuario> buscarPorUnidadeUsuario(Long unidade_id, Long usuario_id);

    @Query(value = "SELECT * FROM unidade_usuario AS udu " +
            "WHERE udu.unidade_id = ?1", nativeQuery = true)
    Optional<List<UnidadeUsuario>> buscarTodosUsuariosDeUmaUnidade(Long unidade_id);

    @Query(value = "DELETE FROM unidade_usuario AS udu " +
            "WHERE udu.unidade_id = ?1 AND udu.usuario_id = ?2", nativeQuery = true)
    Optional<List<UnidadeUsuario>> removerUnidadeDeUmUsuario(Long unidade_id,
                                                             Long usuario_id);
}
