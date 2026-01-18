package com.leonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
  private UUID id;
  private String nombre;
  private String descripcion;

  // Helper constants
  public static final String ADMIN = "ADMIN";
  public static final String RECEPCION = "RECEPCION";
  public static final String BIOANALISTA = "BIOANALISTA";
  public static final String VALIDADOR = "VALIDADOR";
}
