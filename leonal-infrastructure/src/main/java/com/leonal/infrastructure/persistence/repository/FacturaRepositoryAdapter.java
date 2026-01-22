package com.leonal.infrastructure.persistence.repository;

import com.leonal.domain.model.Factura;
import com.leonal.domain.port.output.FacturaRepositoryPort;
import com.leonal.infrastructure.persistence.entity.FacturaEntity;
import com.leonal.infrastructure.persistence.jpa.FacturaJpaRepository;
import com.leonal.infrastructure.persistence.mapper.FacturaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FacturaRepositoryAdapter implements FacturaRepositoryPort {
    private final FacturaJpaRepository jpaRepository;
    private final FacturaMapper mapper;

    @Override
    @Transactional
    public Factura save(Factura factura) {
        FacturaEntity entity = mapper.toEntity(factura);
        FacturaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Factura> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Factura> findByNumero(String numero) {
        return jpaRepository.findByNumero(numero).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> findByOrdenId(UUID ordenId) {
        return jpaRepository.findByOrdenId(ordenId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> findByPacienteId(UUID pacienteId) {
        return jpaRepository.findByPacienteId(pacienteId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> findByEstado(String estado) {
        return jpaRepository.findByEstado(estado).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> findByFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        return jpaRepository.findByFecha(fechaInicio, fechaFin).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Factura> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public String generarProxNumeroFactura() {
        Optional<Integer> maxNumero = jpaRepository.findMaxNumeroSequence();
        int nextNumber = maxNumero.orElse(0) + 1;
        String year = String.valueOf(Year.now().getValue());
        return String.format("FAC-%s-%06d", year, nextNumber);
    }
}
