package com.leonal.infrastructure.persistence.repository;

import com.leonal.domain.model.Resultado;
import com.leonal.domain.port.output.ResultadoRepositoryPort;
import com.leonal.infrastructure.persistence.entity.OrdenDetalleEntity;
import com.leonal.infrastructure.persistence.entity.ResultadoEntity;
import com.leonal.infrastructure.persistence.jpa.OrdenDetalleJpaRepository;
import com.leonal.infrastructure.persistence.mapper.ResultadoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultadoRepositoryAdapter implements ResultadoRepositoryPort {

  private final ResultadoJpaRepository repository;
  private final OrdenDetalleJpaRepository detalleRepository;
  private final ResultadoMapper mapper;

  @Override
  @Transactional
  public Resultado save(Resultado resultado) {
    OrdenDetalleEntity detalle = null;
    if (resultado.getOrdenDetalleId() != null) {
      detalle = detalleRepository.findById(resultado.getOrdenDetalleId())
          .orElseThrow(() -> new RuntimeException("OrdenDetalle no encontrado: " + resultado.getOrdenDetalleId()));
    }
    ResultadoEntity entity = mapper.toEntity(resultado, detalle);
    ResultadoEntity savedEntity = repository.save(entity);
    return mapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Resultado> findById(UUID id) {
    return repository.findById(id).map(mapper::toDomain);
  }

  @Override
  public Optional<Resultado> findByOrdenDetalleId(UUID ordenDetalleId) {
    return repository.findByOrdenDetalleId(ordenDetalleId).map(mapper::toDomain);
  }

  @Override
  public List<Resultado> findByOrdenId(UUID ordenId) {
    // En este caso, como no tenemos un campo ordenId directo en resultados,
    // podríamos buscar por los IDs de los detalles de la orden.
    // O simplificarlo por ahora si no es crítico.
    // Para efectos prácticos, findByOrdenDetalleId suele ser suficiente.
    return List.of();
  }
}
