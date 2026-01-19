package com.leonal.application.dto.examen;

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
public class CreateExamenRequest {
  private UUID id; // Null for new, present for update
  private String codigoInterno;
  private String nombre;
  private String metodo;
  private String tipoResultado;
  private String unidadMedida;
  private BigDecimal precio;
  private boolean activo;
}
