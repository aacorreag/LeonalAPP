package com.leonal.domain.port.output;

import com.leonal.domain.model.CajaSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CajaRepositoryPort {
    CajaSession save(CajaSession cajaSession);
    Optional<CajaSession> findById(UUID id);
    Optional<CajaSession> findByFechaAndCajero(LocalDate fecha, UUID usuarioCajeroId);
    Optional<CajaSession> findAbiertaByCajero(UUID usuarioCajeroId);
    List<CajaSession> findByEstado(String estado);
    List<CajaSession> findByFecha(LocalDate fechaInicio, LocalDate fechaFin);
    List<CajaSession> findByCajero(UUID usuarioCajeroId);
    List<CajaSession> findAll();
    void deleteById(UUID id);
}
