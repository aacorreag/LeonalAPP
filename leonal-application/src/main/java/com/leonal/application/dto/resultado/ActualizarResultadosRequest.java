package com.leonal.application.dto.resultado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarResultadosRequest {
  private UUID ordenId;
  private List<ItemResultadoRequest> resultados;
}
