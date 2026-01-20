package com.leonal.infrastructure.persistence.mapper;

import com.leonal.domain.model.Examen;
import com.leonal.infrastructure.persistence.entity.ExamenEntity;
import org.springframework.stereotype.Component;

@Component
public class ExamenMapper {

  public Examen toDomain(ExamenEntity entity) {
    if (entity == null)
      return null;
    return Examen.builder()
        .id(entity.getId())
        .codigoInterno(entity.getCodigoInterno())
        .nombre(entity.getNombre())
        .metodo(entity.getMetodo())
        .tipoResultado(entity.getTipoResultado())
        .unidadMedida(entity.getUnidadMedida())
        .valoresReferencia(entity.getValoresReferencia())
        .precio(entity.getPrecio())
        .activo(entity.isActivo())
        .build();
  }

  public ExamenEntity toEntity(Examen domain) {
    if (domain == null)
      return null;
    return ExamenEntity.builder()
        .id(domain.getId())
        .codigoInterno(domain.getCodigoInterno())
        .nombre(domain.getNombre())
        .metodo(domain.getMetodo())
        .tipoResultado(domain.getTipoResultado())
        .unidadMedida(domain.getUnidadMedida())
        .valoresReferencia(domain.getValoresReferencia())
        .precio(domain.getPrecio())
        .activo(domain.isActivo())
        .build();
  }
}
