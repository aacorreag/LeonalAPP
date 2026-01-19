package com.leonal.infrastructure.persistence.repository;

import com.leonal.infrastructure.persistence.entity.ExamenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExamenJpaRepository extends JpaRepository<ExamenEntity, UUID> {
  Optional<ExamenEntity> findByCodigoInterno(String codigoInterno);
}
