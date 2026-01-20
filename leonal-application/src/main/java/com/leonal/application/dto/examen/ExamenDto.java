package com.leonal.application.dto.examen;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ExamenDto {
  private UUID id;
  private String codigoInterno;
  private String nombre;
  private String metodos; // Note: In DTO it was 'metodos' or 'metodo'? Checking...
  private String metodo;
  private String tipoResultado;
  private String unidadMedida;
  private String valoresReferencia;
  private BigDecimal precio;
  private boolean activo;
}
