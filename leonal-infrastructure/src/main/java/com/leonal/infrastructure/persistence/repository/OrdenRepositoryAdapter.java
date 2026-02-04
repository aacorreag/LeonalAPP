package com.leonal.infrastructure.persistence.repository;

import com.leonal.domain.model.Orden;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import com.leonal.infrastructure.persistence.entity.OrdenEntity;
import com.leonal.infrastructure.persistence.entity.PacienteEntity;
import com.leonal.infrastructure.persistence.jpa.OrdenJpaRepository;
import com.leonal.infrastructure.persistence.repository.PacienteJpaRepository;
import com.leonal.infrastructure.persistence.mapper.OrdenMapper;
import com.leonal.infrastructure.persistence.mapper.PacienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrdenRepositoryAdapter implements OrdenRepositoryPort {

  private final OrdenJpaRepository ordenJpaRepository;
  private final PacienteJpaRepository pacienteJpaRepository;
  private final OrdenMapper ordenMapper;
  private final PacienteMapper pacienteMapper;

  @Override
  @Transactional
  public Orden save(@NonNull Orden orden) {
    // Resolver PacienteEntity
    PacienteEntity pacienteEntity;
    if (orden.getPaciente().getId() != null) {
      pacienteEntity = pacienteJpaRepository.findById(orden.getPaciente().getId())
          .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
    } else {
      // Paciente nuevo - guardar primero
      pacienteEntity = pacienteJpaRepository.save(pacienteMapper.toEntity(orden.getPaciente()));
    }

    OrdenEntity entity = ordenMapper.toEntity(orden, pacienteEntity);
    OrdenEntity saved = ordenJpaRepository.save(entity);
    return ordenMapper.toDomain(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Orden> findById(@NonNull UUID id) {
    return ordenJpaRepository.findById(id)
        .map(ordenMapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Orden> findAll() {
    return ordenJpaRepository.findAllByOrderByFechaRecepcionDesc()
        .stream()
        .map(ordenMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Orden> findByPacienteId(@NonNull UUID pacienteId) {
    return ordenJpaRepository.findByPacienteIdOrderByFechaRecepcionDesc(pacienteId)
        .stream()
        .map(ordenMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public long countByFecha(@NonNull LocalDate fecha) {
    LocalDateTime startOfDay = fecha.atStartOfDay();
    LocalDateTime endOfDay = fecha.atTime(LocalTime.MAX);
    return ordenJpaRepository.countByFechaRecepcionBetween(startOfDay, endOfDay);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Orden> findAllUnbilled() {
    return ordenJpaRepository.findUnbilled()
        .stream()
        .map(ordenMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public long countPendientes() {
    // Definir qué estados se consideran "pendientes"
    // Si no está el Enum, usamos Strings.
    // Asumiremos todo lo que no esté "COMPLETADA" o "ENTREGADA"
    return ordenJpaRepository.countByEstadoIn(List.of("PENDIENTE", "REGISTRADA", "EN_PROCESO"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Object[]> countOrdenesPorDiaUltimaSemana() {
    LocalDateTime haceSieteDias = LocalDateTime.now().minusDays(6).with(LocalTime.MIN);
    return ordenJpaRepository.countOrdenesPorDiaDesde(haceSieteDias);
  }
}
