package com.leonal.infrastructure.persistence.repository;

import com.leonal.domain.model.Paciente;
import com.leonal.domain.port.output.PacienteRepositoryPort;
import com.leonal.infrastructure.persistence.entity.PacienteEntity;
import com.leonal.infrastructure.persistence.mapper.PacienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PacienteRepositoryAdapter implements PacienteRepositoryPort {

  private final PacienteJpaRepository jpaRepository;
  private final PacienteMapper mapper;

  @Override
  @Transactional
  public Paciente save(Paciente paciente) {
    PacienteEntity entity = mapper.toEntity(paciente);
    PacienteEntity saved = jpaRepository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Paciente> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Paciente> findByDocumento(String tipoDocumento, String numeroDocumento) {
    return jpaRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)
        .map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Paciente> findAll() {
    return jpaRepository.findAll().stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Paciente> search(String query) {
    return jpaRepository.search(query).stream()
        .map(mapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
