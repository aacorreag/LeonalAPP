package com.leonal.application.dto.resultado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResultadoRequest {
  private UUID ordenDetalleId;
  private String valor;
  private String observacionReporte;
  private boolean esPatologico;
}
