package com.leonal.infrastructure.persistence.repository;

import com.leonal.domain.model.Pago;
import com.leonal.domain.port.output.PagoRepositoryPort;
import com.leonal.infrastructure.persistence.entity.PagoEntity;
import com.leonal.infrastructure.persistence.jpa.PagoJpaRepository;
import com.leonal.infrastructure.persistence.mapper.PagoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PagoRepositoryAdapter implements PagoRepositoryPort {
    private final PagoJpaRepository jpaRepository;
    private final PagoMapper mapper;

    @Override
    @Transactional
    public Pago save(Pago pago) {
        PagoEntity entity = mapper.toEntity(pago);
        PagoEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> findByFacturaId(UUID facturaId) {
        return jpaRepository.findByFacturaId(facturaId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> findByEstado(String estado) {
        return jpaRepository.findByEstado(estado).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> findByFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return jpaRepository.findByFecha(fechaInicio, fechaFin).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> findAll() {
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
