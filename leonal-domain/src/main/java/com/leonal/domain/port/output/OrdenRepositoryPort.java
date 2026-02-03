package com.leonal.domain.port.output;

import com.leonal.domain.model.Orden;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdenRepositoryPort {
  Orden save(Orden orden);

  Optional<Orden> findById(UUID id);

  List<Orden> findAll();

  List<Orden> findByPacienteId(UUID pacienteId);

  long countByFecha(java.time.LocalDate fecha);

  List<Orden> findAllUnbilled();
}
