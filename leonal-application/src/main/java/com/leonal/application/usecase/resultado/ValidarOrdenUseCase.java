package com.leonal.application.usecase.resultado;

import com.leonal.domain.model.Orden;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ValidarOrdenUseCase {

  private final OrdenRepositoryPort ordenRepository;

  public void execute(UUID ordenId, UUID validadorId) {
    Orden orden = ordenRepository.findById(ordenId)
        .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

    // Aquí se podrían agregar validaciones adicionales,
    // por ejemplo verificar que todos los exámenes tengan resultados.

    orden.setEstado("VALIDADO");
    // Nota: En una fase real, guardaríamos quién validó y cuándo en la orden o en
    // auditoría.
    ordenRepository.save(orden);
  }
}
