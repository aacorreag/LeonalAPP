package com.leonal.application.dto.auth;

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
public class AuthenticatedUser {
  private UUID id;
  private String username;
  private String nombreCompleto;
  private String email;
  private List<String> roles; // List of Role Names for logic
}
