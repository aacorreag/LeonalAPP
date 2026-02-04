package com.leonal.application.usecase.dashboard;

import com.leonal.application.dto.dashboard.DashboardStatsDto;
import com.leonal.domain.port.output.FacturaRepositoryPort;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ObtenerDashboardStatsUseCase {

  private final OrdenRepositoryPort ordenRepository;
  private final FacturaRepositoryPort facturaRepository;

  public DashboardStatsDto execute() {
    LocalDate hoy = LocalDate.now();

    // 1. Ingresos Hoy
    BigDecimal ingresosHoy = facturaRepository.sumTotalByFecha(hoy);
    if (ingresosHoy == null) {
      ingresosHoy = BigDecimal.ZERO;
    }

    // 2. Órdenes Hoy
    long ordenesHoy = ordenRepository.countByFecha(hoy);

    // 3. Pendientes
    long pendientes = ordenRepository.countPendientes();

    // 4. Actividad Semanal
    List<Object[]> statsSemana = ordenRepository.countOrdenesPorDiaUltimaSemana();
    Map<String, Long> mapaSemana = new LinkedHashMap<>();

    // Inicializar mapa vacío para asegurar orden
    // (La implementación real dependerá de lo que devuelva el repo,
    // pero aquí transformamos la lista cruda a un mapa usable por la UI)
    if (statsSemana != null) {
      for (Object[] row : statsSemana) {
        // Asumiendo [0] = Dia (String), [1] = Cantidad (Long/Number)
        String dia = row[0].toString();
        Number cantidad = (Number) row[1];
        mapaSemana.put(dia, cantidad.longValue());
      }
    }

    return DashboardStatsDto.builder()
        .ingresosHoy(ingresosHoy)
        .ordenesHoy(ordenesHoy)
        .resultadosPendientes(pendientes)
        .ordenesSemana(mapaSemana)
        .build();
  }
}
