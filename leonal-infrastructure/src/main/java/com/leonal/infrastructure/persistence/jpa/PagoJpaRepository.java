package com.leonal.infrastructure.persistence.jpa;

import com.leonal.infrastructure.persistence.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PagoJpaRepository extends JpaRepository<PagoEntity, UUID> {
    List<PagoEntity> findByFacturaId(UUID facturaId);

    List<PagoEntity> findByEstado(String estado);

    @Query("SELECT p FROM PagoEntity p WHERE p.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<PagoEntity> findByFecha(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}
