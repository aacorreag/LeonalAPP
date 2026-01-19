package com.leonal.infrastructure.persistence.repository;

import com.leonal.domain.model.Rol;
import com.leonal.domain.port.output.RolRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RolRepositoryAdapter implements RolRepositoryPort {
  private final RolJpaRepository jpaRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Rol> findAll() {
    return jpaRepository.findAll().stream()
        .map(entity -> Rol.builder()
            .id(entity.getId())
            .nombre(entity.getNombre())
            .descripcion(entity.getDescripcion())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Rol> findByName(String nombre) {
    return jpaRepository.findByNombre(nombre)
        .map(entity -> Rol.builder()
            .id(entity.getId())
            .nombre(entity.getNombre())
            .descripcion(entity.getDescripcion())
            .build());
  }
}
