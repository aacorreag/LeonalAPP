package com.leonal.application.dto.dashboard;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDto {
  private BigDecimal ingresosHoy;
  private long ordenesHoy;
  private long resultadosPendientes;
  private Map<String, Long> ordenesSemana; // Key: Día (e.g., "Lunes"), Value: Cantidad
}
