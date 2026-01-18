package com.leonal.application.usecase.auth;

import com.leonal.application.dto.auth.AuthenticatedUser;
import com.leonal.application.dto.auth.LoginRequest;
import com.leonal.domain.model.Usuario;
import com.leonal.domain.port.output.PasswordEncoderPort;
import com.leonal.domain.port.output.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AutenticarUsuarioUseCase {

  private final UsuarioRepositoryPort usuarioRepository;
  private final PasswordEncoderPort passwordEncoder;

  public AuthenticatedUser execute(LoginRequest request) {
    return usuarioRepository.findByUsername(request.getUsername())
        .filter(u -> u.isActivo())
        .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPasswordHash()))
        .map(this::mapToDto)
        .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
  }

  private AuthenticatedUser mapToDto(Usuario usuario) {
    return AuthenticatedUser.builder()
        .id(usuario.getId())
        .username(usuario.getUsername())
        .nombreCompleto(usuario.getNombreCompleto())
        .email(usuario.getEmail())
        .roles(usuario.getRoles().stream()
            .map(r -> r.getNombre())
            .collect(Collectors.toList()))
        .build();
  }
}
