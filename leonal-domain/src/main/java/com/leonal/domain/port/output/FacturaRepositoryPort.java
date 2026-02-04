package com.leonal.domain.port.output;

import com.leonal.domain.model.Factura;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacturaRepositoryPort {
    Factura save(Factura factura);

    Optional<Factura> findById(UUID id);

    Optional<Factura> findByNumero(String numero);

    List<Factura> findByOrdenId(UUID ordenId);

    List<Factura> findByPacienteId(UUID pacienteId);

    List<Factura> findByEstado(String estado);

    List<Factura> findByFecha(LocalDate fechaInicio, LocalDate fechaFin);

    List<Factura> findAll();

    void deleteById(UUID id);

    String generarProxNumeroFactura();

    java.math.BigDecimal sumTotalByFecha(LocalDate fecha);
}
