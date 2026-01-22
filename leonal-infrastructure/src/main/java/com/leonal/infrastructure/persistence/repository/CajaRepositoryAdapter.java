package com.leonal.infrastructure.persistence.repository;

import com.leonal.domain.model.CajaSession;
import com.leonal.domain.port.output.CajaRepositoryPort;
import com.leonal.infrastructure.persistence.entity.CajaSessionEntity;
import com.leonal.infrastructure.persistence.jpa.CajaJpaRepository;
import com.leonal.infrastructure.persistence.mapper.CajaSessionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CajaRepositoryAdapter implements CajaRepositoryPort {
    private final CajaJpaRepository jpaRepository;
    private final CajaSessionMapper mapper;

    @Override
    @Transactional
    public CajaSession save(CajaSession cajaSession) {
        CajaSessionEntity entity = mapper.toEntity(cajaSession);
        CajaSessionEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaSession> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaSession> findByFechaAndCajero(LocalDate fecha, UUID usuarioCajeroId) {
        return jpaRepository.findByFechaAndCajero(fecha, usuarioCajeroId).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaSession> findAbiertaByCajero(UUID usuarioCajeroId) {
        return jpaRepository.findAbiertaByCajero(usuarioCajeroId).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaSession> findByEstado(String estado) {
        return jpaRepository.findByEstado(estado).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaSession> findByFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        return jpaRepository.findByFecha(fechaInicio, fechaFin).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaSession> findByCajero(UUID usuarioCajeroId) {
        return jpaRepository.findByUsuarioCajeroId(usuarioCajeroId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaSession> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
