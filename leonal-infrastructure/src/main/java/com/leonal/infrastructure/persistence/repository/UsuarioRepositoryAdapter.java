package com.leonal.infrastructure.persistence.repository;

import com.leonal.domain.model.Usuario;
import com.leonal.domain.port.output.UsuarioRepositoryPort;
import com.leonal.infrastructure.persistence.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

  private final UsuarioJpaRepository jpaRepository;
  private final UsuarioMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public Optional<Usuario> findByUsername(String username) {
    return jpaRepository.findByUsername(username)
        .map(mapper::toDomain);
  }
}
