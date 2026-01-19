package com.leonal.infrastructure.persistence.repository;

import com.leonal.domain.model.Usuario;
import com.leonal.domain.port.output.UsuarioRepositoryPort;
import com.leonal.infrastructure.persistence.entity.UsuarioEntity;
import com.leonal.infrastructure.persistence.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

  @Override
  @Transactional(readOnly = true)
  public List<Usuario> findAll() {
    return jpaRepository.findAll().stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Usuario> findById(UUID id) {
    return jpaRepository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional
  public Usuario save(Usuario usuario) {
    UsuarioEntity entity = mapper.toEntity(usuario);
    UsuarioEntity saved = jpaRepository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    jpaRepository.deleteById(id);
  }
}
