package com.leonal.application.dto.orden;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDto {
  private UUID id;
  private String codigoOrden;
  private UUID pacienteId;
  private String pacienteNombre;
  private String pacienteDocumento;
  private LocalDateTime fechaRecepcion;
  private String estado;
  private BigDecimal total;
  private int itemCount;
  private List<OrdenDetalleDto> detalles;
}
