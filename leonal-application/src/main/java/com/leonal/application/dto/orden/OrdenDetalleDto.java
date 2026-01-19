package com.leonal.application.dto.orden;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDetalleDto {
  private UUID id;
  private UUID examenId;
  private String examenCodigo;
  private String examenNombre;
  private BigDecimal precioCobrado;
  private String estado;
}
