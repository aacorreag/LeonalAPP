package com.leonal.infrastructure.persistence.repository;

import com.leonal.domain.model.Examen;
import com.leonal.domain.port.output.ExamenRepositoryPort;
import com.leonal.infrastructure.persistence.entity.ExamenEntity;
import com.leonal.infrastructure.persistence.mapper.ExamenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExamenRepositoryAdapter implements ExamenRepositoryPort {

  private final ExamenJpaRepository jpaRepository;
  private final ExamenMapper mapper;

  @Override
  public List<Examen> findAll() {
    return jpaRepository.findAll().stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Examen> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Examen save(Examen examen) {
    ExamenEntity entity = mapper.toEntity(examen);
    ExamenEntity saved = jpaRepository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public Optional<Examen> findByCodigoInterno(String codigoInterno) {
    return jpaRepository.findByCodigoInterno(codigoInterno).map(mapper::toDomain);
  }
}
