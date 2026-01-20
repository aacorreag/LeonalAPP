package com.leonal.application.usecase.resultado;

import com.leonal.domain.model.Orden;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import com.leonal.domain.port.output.ReportRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GenerarReporteResultadosUseCase {

  private final ReportRepositoryPort reportRepositoryPort;
  private final OrdenRepositoryPort ordenRepositoryPort;

  public byte[] execute(UUID ordenId) {
    Orden orden = ordenRepositoryPort.findById(ordenId)
        .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + ordenId));

    return reportRepositoryPort.generateResultadosReport(orden);
  }
}
