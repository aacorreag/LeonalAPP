package com.leonal.infrastructure.persistence.repository;

import com.leonal.infrastructure.persistence.entity.ResultadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ResultadoJpaRepository extends JpaRepository<ResultadoEntity, UUID> {
  Optional<ResultadoEntity> findByOrdenDetalleId(UUID ordenDetalleId);
}
