package com.leonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

  @Builder.Default
  private List<Rol> roles = new ArrayList<>();

  public boolean hasRol(String rolNombre) {
    if (roles == null)
      return false;
    return roles.stream().anyMatch(r -> r.getNombre().equals(rolNombre));
  }
}
