package com.leonal.infrastructure.persistence.repository;

import com.leonal.infrastructure.persistence.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface PacienteJpaRepository extends JpaRepository<PacienteEntity, UUID> {
  Optional<PacienteEntity> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);

  @Query("SELECT p FROM PacienteEntity p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR p.numeroDocumento LIKE CONCAT('%', :query, '%')")
  List<PacienteEntity> search(String query);
}
