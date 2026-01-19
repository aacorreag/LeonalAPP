package com.leonal.infrastructure.persistence.mapper;

import com.leonal.domain.model.Rol;
import com.leonal.domain.model.Usuario;
import com.leonal.infrastructure.persistence.entity.RolEntity;
import com.leonal.infrastructure.persistence.entity.UsuarioEntity;
import com.leonal.infrastructure.persistence.repository.RolJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

  private final RolJpaRepository rolRepository;

  public Usuario toDomain(UsuarioEntity entity) {
    if (entity == null)
      return null;
    return Usuario.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .passwordHash(entity.getPasswordHash())
        .nombreCompleto(entity.getNombreCompleto())
        .email(entity.getEmail())
        .activo(entity.isActivo())
        .createdAt(entity.getCreatedAt())
        .roles(entity.getRoles() != null ? entity.getRoles().stream()
            .map(this::toDomainRol)
            .collect(Collectors.toList()) : Collections.emptyList())
        .build();
  }

  public UsuarioEntity toEntity(Usuario domain) {
    if (domain == null)
      return null;

    UsuarioEntity entity = UsuarioEntity.builder()
        .id(domain.getId())
        .username(domain.getUsername())
        .passwordHash(domain.getPasswordHash())
        .nombreCompleto(domain.getNombreCompleto())
        .email(domain.getEmail())
        .activo(domain.isActivo())
        .createdAt(domain.getCreatedAt())
        .build();

    // Map Roles Domain (Names/IDs) to Entity (fetching refs)
    // This assumes the Domain User object has Roles populated with valid names or
    // IDs.
    // In GuardarUsuarioUseCase we populate Roles. Here we need to map them back to
    // Entities.
    // Ideally, we fetch by ID. Here we rely on finding by Name from domain model.
    if (domain.getRoles() != null) {
      entity.setRoles(domain.getRoles().stream()
          .map(r -> rolRepository.findByNombre(r.getNombre()).orElse(null))
          .filter(r -> r != null)
          .collect(Collectors.toList()));
    }

    return entity;
  }

  private Rol toDomainRol(RolEntity entity) {
    return Rol.builder()
        .id(entity.getId())
        .nombre(entity.getNombre())
        .descripcion(entity.getDescripcion())
        .build();
  }
}
