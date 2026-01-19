package com.leonal.application.dto.user;

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
public class CreateUserRequest {
  private UUID id; // If present, update
  private String username;
  private String password; // Optional if update
  private String nombreCompleto;
  private String email;
  private boolean activo;
  private List<String> roles; // List of Role Names
}
