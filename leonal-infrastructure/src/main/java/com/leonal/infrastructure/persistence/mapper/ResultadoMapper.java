package com.leonal.infrastructure.persistence.mapper;

import com.leonal.domain.model.Resultado;
import com.leonal.domain.model.ResultadoVersion;
import com.leonal.infrastructure.persistence.entity.OrdenDetalleEntity;
import com.leonal.infrastructure.persistence.entity.ResultadoEntity;
import com.leonal.infrastructure.persistence.entity.ResultadoVersionEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ResultadoMapper {

  public Resultado toDomain(ResultadoEntity entity) {
    if (entity == null)
      return null;
    return Resultado.builder()
        .id(entity.getId())
        .ordenDetalleId(entity.getOrdenDetalle() != null ? entity.getOrdenDetalle().getId() : null)
        .valor(entity.getValor())
        .observacionInterna(entity.getObservacionInterna())
        .observacionReporte(entity.getObservacionReporte())
        .esPatologico(entity.isEsPatologico())
        .fechaResultado(entity.getFechaResultado())
        .usuarioResultadoId(entity.getUsuarioResultadoId())
        .fechaValidacion(entity.getFechaValidacion())
        .usuarioValidacionId(entity.getUsuarioValidacionId())
        .versiones(entity.getVersiones().stream()
            .map(this::toDomainVersion)
            .collect(Collectors.toList()))
        .build();
  }

  public ResultadoVersion toDomainVersion(ResultadoVersionEntity entity) {
    if (entity == null)
      return null;
    return ResultadoVersion.builder()
        .id(entity.getId())
        .resultadoId(entity.getResultado() != null ? entity.getResultado().getId() : null)
        .valorAnterior(entity.getValorAnterior())
        .motivoCambio(entity.getMotivoCambio())
        .usuarioModificoId(entity.getUsuarioModificoId())
        .fechaCambio(entity.getFechaCambio())
        .build();
  }

  public ResultadoEntity toEntity(Resultado domain) {
    return toEntity(domain, null);
  }

  public ResultadoEntity toEntity(Resultado domain, OrdenDetalleEntity ordenDetalle) {
    if (domain == null)
      return null;
    ResultadoEntity entity = ResultadoEntity.builder()
        .id(domain.getId())
        .ordenDetalle(ordenDetalle)
        .valor(domain.getValor())
        .observacionInterna(domain.getObservacionInterna())
        .observacionReporte(domain.getObservacionReporte())
        .esPatologico(domain.isEsPatologico())
        .fechaResultado(domain.getFechaResultado())
        .usuarioResultadoId(domain.getUsuarioResultadoId())
        .fechaValidacion(domain.getFechaValidacion())
        .usuarioValidacionId(domain.getUsuarioValidacionId())
        .build();

    if (domain.getVersiones() != null) {
      entity.setVersiones(domain.getVersiones().stream()
          .map(v -> toEntityVersion(v, entity))
          .collect(Collectors.toList()));
    }
    return entity;
  }

  public ResultadoVersionEntity toEntityVersion(ResultadoVersion domain, ResultadoEntity parent) {
    if (domain == null)
      return null;
    return ResultadoVersionEntity.builder()
        .id(domain.getId())
        .resultado(parent)
        .valorAnterior(domain.getValorAnterior())
        .motivoCambio(domain.getMotivoCambio())
        .usuarioModificoId(domain.getUsuarioModificoId())
        .fechaCambio(domain.getFechaCambio())
        .build();
  }
}
