package com.leonal.application.usecase.factura;

import com.leonal.domain.model.Factura;
import com.leonal.domain.model.Orden;
import com.leonal.domain.port.output.FacturaRepositoryPort;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import com.leonal.domain.port.output.ReportRepositoryPort;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@RequiredArgsConstructor
public class GenerarReporteFacturaUseCase {

  private final FacturaRepositoryPort facturaRepository;
  private final OrdenRepositoryPort ordenRepository;
  private final ReportRepositoryPort reportRepository;

  public byte[] execute(UUID facturaId) {
    Factura factura = facturaRepository.findById(facturaId)
        .orElseThrow(() -> new RuntimeException("Factura no encontrada: " + facturaId));

    Orden orden = ordenRepository.findById(factura.getOrdenId())
        .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + factura.getOrdenId()));

    return reportRepository.generateFacturaReport(factura, orden);
  }
}
