package com.leonal.application.usecase.user;

import com.leonal.application.dto.user.CreateUserRequest;
import com.leonal.domain.model.Rol;
import com.leonal.domain.model.Usuario;
import com.leonal.domain.port.output.PasswordEncoderPort;
import com.leonal.domain.port.output.RolRepositoryPort;
import com.leonal.domain.port.output.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GuardarUsuarioUseCase {
  private final UsuarioRepositoryPort usuarioRepository;
  private final RolRepositoryPort rolRepository;
  private final PasswordEncoderPort passwordEncoder;

  public void execute(CreateUserRequest request) {
    Usuario usuario;

    // Load existing or create new
    if (request.getId() != null) {
      usuario = usuarioRepository.findById(request.getId())
          .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    } else {
      usuario = new Usuario();
      usuario.setCreatedAt(LocalDateTime.now());
      // New user requires password
      if (request.getPassword() == null || request.getPassword().isEmpty()) {
        throw new IllegalArgumentException("La contraseña es obligatoria para nuevos usuarios");
      }
    }

    // Update basic fields
    usuario.setUsername(request.getUsername());
    usuario.setNombreCompleto(request.getNombreCompleto());
    usuario.setEmail(request.getEmail());
    usuario.setActivo(request.isActivo());

    // Update Password if provided
    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    }

    // Update Roles
    List<Rol> roles = new ArrayList<>();
    if (request.getRoles() != null) {
      for (String rolName : request.getRoles()) {
        Optional<Rol> rolOpt = rolRepository.findByName(rolName);
        rolOpt.ifPresent(roles::add);
      }
    }
    usuario.setRoles(roles);

    // Save
    usuarioRepository.save(usuario);
  }
}
