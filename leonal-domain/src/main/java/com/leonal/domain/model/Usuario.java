package com.leonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
  private UUID id;
  private String username;
  private String passwordHash;
  private String nombreCompleto;
  private String email;
  private boolean activo;
  private LocalDateTime createdAt;
}
