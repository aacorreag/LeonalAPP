package com.leonal.infrastructure.persistence.mapper;

import com.leonal.domain.model.Rol;
import com.leonal.domain.model.Usuario;
import com.leonal.infrastructure.persistence.entity.RolEntity;
import com.leonal.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

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
        .roles(entity.getRoles().stream()
            .map(this::toDomainRol)
            .collect(Collectors.toList()))
        .build();
  }

  // No reverse mapping needed for Login, but good practice
  public UsuarioEntity toEntity(Usuario domain) {
    if (domain == null)
      return null;
    // Note: Mapping Roles back to Entity requires fetching refs, skipping for
    // read-only login scope
    return UsuarioEntity.builder()
        .id(domain.getId())
        .username(domain.getUsername())
        .passwordHash(domain.getPasswordHash())
        .nombreCompleto(domain.getNombreCompleto())
        .email(domain.getEmail())
        .activo(domain.isActivo())
        .createdAt(domain.getCreatedAt())
        .build();
  }

  private Rol toDomainRol(RolEntity entity) {
    return Rol.builder()
        .id(entity.getId())
        .nombre(entity.getNombre())
        .descripcion(entity.getDescripcion())
        .build();
  }
}
