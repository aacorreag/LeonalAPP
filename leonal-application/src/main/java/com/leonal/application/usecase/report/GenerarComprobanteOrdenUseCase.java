package com.leonal.application.usecase.report;

import com.leonal.domain.model.Orden;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import com.leonal.domain.port.output.ReportRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GenerarComprobanteOrdenUseCase {

  private final OrdenRepositoryPort ordenRepository;
  private final ReportRepositoryPort reportRepository;

  public byte[] ejecutar(UUID ordenId) {
    Orden orden = ordenRepository.findById(ordenId)
        .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + ordenId));

    return reportRepository.generateOrdenReport(orden);
  }
}
