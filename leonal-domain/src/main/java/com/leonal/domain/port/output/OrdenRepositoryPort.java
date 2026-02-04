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

  long countPendientes();

  // Retorna una lista de objetos donde [0] = Dia (String), [1] = Cantidad (Long)
  List<Object[]> countOrdenesPorDiaUltimaSemana();

  List<Orden> findAllUnbilled();
}
