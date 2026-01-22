package com.leonal.domain.port.output;

import com.leonal.domain.model.Pago;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PagoRepositoryPort {
    Pago save(Pago pago);
    Optional<Pago> findById(UUID id);
    List<Pago> findByFacturaId(UUID facturaId);
    List<Pago> findByEstado(String estado);
    List<Pago> findByFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Pago> findAll();
    void deleteById(UUID id);
}
