package com.leonal.infrastructure.persistence.jpa;

import com.leonal.infrastructure.persistence.entity.FacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FacturaJpaRepository extends JpaRepository<FacturaEntity, UUID> {
    Optional<FacturaEntity> findByNumero(String numero);

    List<FacturaEntity> findByOrdenId(UUID ordenId);

    List<FacturaEntity> findByPacienteId(UUID pacienteId);

    List<FacturaEntity> findByEstado(String estado);

    @Query("SELECT f FROM FacturaEntity f WHERE f.fechaEmision BETWEEN :fechaInicio AND :fechaFin")
    List<FacturaEntity> findByFecha(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT MAX(CAST(SUBSTRING(f.numero, 10) AS INTEGER)) FROM FacturaEntity f")
    Optional<Integer> findMaxNumeroSequence();

    @Query("SELECT SUM(f.total) FROM FacturaEntity f WHERE f.fechaEmision = :fecha AND f.estado <> 'ANULADA'")
    java.math.BigDecimal sumTotalByFecha(@Param("fecha") java.time.LocalDate fecha);
}
