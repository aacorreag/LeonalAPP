package com.leonal.application.usecase.user;

import com.leonal.application.dto.user.UserDto;
import com.leonal.domain.model.Usuario;
import com.leonal.domain.port.output.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListarUsuariosUseCase {
  private final UsuarioRepositoryPort usuarioRepository;

  public List<UserDto> execute() {
    return usuarioRepository.findAll().stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  private UserDto mapToDto(Usuario u) {
    return UserDto.builder()
        .id(u.getId())
        .username(u.getUsername())
        .nombreCompleto(u.getNombreCompleto())
        .email(u.getEmail())
        .activo(u.isActivo())
        .createdAt(u.getCreatedAt())
        .roles(u.getRoles().stream()
            .map(r -> r.getNombre())
            .collect(Collectors.toList()))
        .build();
  }
}
