package com.leonal.infrastructure.persistence.jpa;

import com.leonal.infrastructure.persistence.entity.OrdenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrdenJpaRepository extends JpaRepository<OrdenEntity, UUID> {

  List<OrdenEntity> findByPacienteIdOrderByFechaRecepcionDesc(UUID pacienteId);

  @Query("SELECT COUNT(o) FROM OrdenEntity o WHERE o.fechaRecepcion >= :startOfDay AND o.fechaRecepcion < :endOfDay")
  long countByFechaRecepcionBetween(@Param("startOfDay") LocalDateTime startOfDay,
      @Param("endOfDay") LocalDateTime endOfDay);

  List<OrdenEntity> findAllByOrderByFechaRecepcionDesc();

  @Query("SELECT o FROM OrdenEntity o WHERE o.id NOT IN (SELECT f.ordenId FROM FacturaEntity f WHERE f.estado != 'ANULADA') ORDER BY o.fechaRecepcion DESC")
  List<OrdenEntity> findUnbilled();
}
