package com.leonal.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private UUID id;
  private String username;
  private String nombreCompleto;
  private String email;
  private boolean activo;
  private LocalDateTime createdAt;
  private List<String> roles;
}
