package com.leonal.infrastructure.persistence.jpa;

import com.leonal.infrastructure.persistence.entity.CajaSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CajaJpaRepository extends JpaRepository<CajaSessionEntity, UUID> {
    @Query("SELECT c FROM CajaSessionEntity c WHERE c.fecha = :fecha AND c.usuarioCajeroId = :usuarioCajeroId")
    Optional<CajaSessionEntity> findByFechaAndCajero(@Param("fecha") LocalDate fecha, @Param("usuarioCajeroId") UUID usuarioCajeroId);

    @Query("SELECT c FROM CajaSessionEntity c WHERE c.estado = 'ABIERTA' AND c.usuarioCajeroId = :usuarioCajeroId")
    Optional<CajaSessionEntity> findAbiertaByCajero(@Param("usuarioCajeroId") UUID usuarioCajeroId);

    List<CajaSessionEntity> findByEstado(String estado);

    @Query("SELECT c FROM CajaSessionEntity c WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<CajaSessionEntity> findByFecha(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    List<CajaSessionEntity> findByUsuarioCajeroId(UUID usuarioCajeroId);
}
