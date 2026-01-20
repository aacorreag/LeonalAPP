package com.leonal.domain.port.output;

import com.leonal.domain.model.Orden;

public interface ReportRepositoryPort {
  byte[] generateOrdenReport(Orden orden);

  byte[] generateResultadosReport(Orden orden);
}
