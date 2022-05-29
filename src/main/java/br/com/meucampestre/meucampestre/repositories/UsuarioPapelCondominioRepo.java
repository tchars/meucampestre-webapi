package br.com.meucampestre.meucampestre.repositories;

import br.com.meucampestre.meucampestre.domain.models.UsuarioPapelCondominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

public interface UsuarioPapelCondominioRepo extends JpaRepository<UsuarioPapelCondominio, Long> {

    @Query(value = "SELECT * FROM usuario_papel_condominio AS upcl " +
            "WHERE upcl.usuario_id = ?1 AND upcl.condominio_id = ?2 AND upcl.papel_id = ?3",
            nativeQuery = true)
    UsuarioPapelCondominio buscarPorUsuarioCondominioPapel(Long usuario_id,
                                                           Long condominio_id,
                                                           Long papel_id);

    @Query(value = "SELECT * FROM usuario_papel_condominio AS upcl " +
            "WHERE upcl.usuario_id = ?1 AND upcl.condominio_id = ?2 AND upcl.papel_id = ?3",
            nativeQuery = true)
    Optional<UsuarioPapelCondominio> getPorUsuarioCondominioPapel(Long usuario_id,
                                                                 Long condominio_id,
                                                                 Long papel_id);

    @Query(value = "SELECT * FROM usuario_papel_condominio AS upcl " +
            "WHERE upcl.condominio_id = ?1", nativeQuery = true)
    Collection<UsuarioPapelCondominio> buscarTodosUsuariosDeUmCondominio(Long condominio_id);

    @Query(value = "SELECT * FROM usuario_papel_condominio AS upcl " +
            "WHERE upcl.usuario_id = ?1", nativeQuery = true)
    Collection<UsuarioPapelCondominio> buscarPorUsuario(Long usuario_id);

    @Modifying
    @Query(value = "DELETE FROM `usuario_papel_condominio` WHERE usuario_id = ?1 AND condominio_id = ?2", nativeQuery = true)
    @Transactional
    void apagarTodasPermissoesDoUsuarioAoCondominio(Long usuario_id, Long condominio_id);

    @Modifying
    @Query(value = "INSERT INTO `usuario_papel_condominio`(id, usuario_id, condominio_id, papel_id) VALUES (null, ?1 , ?2, ?3)", nativeQuery = true)
    @Transactional
    void inserirPermissoesDoUsuarioAoCondominio(Long usuario_id, Long condominio_id, Long papel_id);

    @Query(value = "SELECT * FROM usuario_papel_condominio AS upcl " +
            "WHERE upcl.usuario_id = ?1 AND upcl.condominio_id = ?2", nativeQuery = true)
    Collection<UsuarioPapelCondominio> buscarPorUsuarioECondominio(Long usuario_id, Long idCondominio);
}
