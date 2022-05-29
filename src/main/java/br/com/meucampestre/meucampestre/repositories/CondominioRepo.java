package br.com.meucampestre.meucampestre.repositories;

import br.com.meucampestre.meucampestre.domain.models.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CondominioRepo extends JpaRepository<Condominio, Long> {

//    @Query(value = "SELECT cond.* " +
//            "FROM condominio cond, condominio_usuarios condU, usuario u " +
//            "WHERE condU.condominio_id = cond.id " +
//            "AND condU.usuarios_id = u.id " +
//            "AND u.id = ?1", nativeQuery = true)
//    List<Condominio> getCondominiosPorIdUsuario(String usuarioId);
//
//    @Query(value = "SELECT cond.* FROM condominio cond, condominio_usuarios condU, usuario u " +
//            "WHERE condU.condominio_id = cond.id " +
//            "AND condU.usuarios_id = u.id " +
//            "AND u.documento = ?1", nativeQuery = true)
//    List<Condominio> getCondominiosPorDocumentoUsuario(String documentoUsuario);

    @Query(value = "SELECT cond.* FROM condominio cond WHERE cond.documento = ?1", nativeQuery = true)
    Optional<Condominio> getCondominioPorDocumento(String documento);

    @Query(value = "SELECT cond.* FROM condominio cond WHERE cond.id = ?1", nativeQuery = true)
    Optional<Condominio> buscarPeloId(Long id);
}
