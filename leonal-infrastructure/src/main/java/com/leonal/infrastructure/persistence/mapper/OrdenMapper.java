package com.leonal.infrastructure.persistence.mapper;

import com.leonal.domain.model.Orden;
import com.leonal.domain.model.OrdenDetalle;
import com.leonal.infrastructure.persistence.entity.ExamenEntity;
import com.leonal.infrastructure.persistence.entity.OrdenDetalleEntity;
import com.leonal.infrastructure.persistence.entity.OrdenEntity;
import com.leonal.infrastructure.persistence.entity.PacienteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrdenMapper {

  private final PacienteMapper pacienteMapper;
  private final ExamenMapper examenMapper;

  public Orden toDomain(OrdenEntity entity) {
    if (entity == null)
      return null;

    List<OrdenDetalle> detalles = entity.getDetalles() != null
        ? entity.getDetalles().stream().map(this::toDetalleDomain).collect(Collectors.toList())
        : new ArrayList<>();

    return Orden.builder()
        .id(entity.getId())
        .codigoOrden(entity.getCodigoOrden())
        .numeroOrden(entity.getNumeroOrden())
        .paciente(pacienteMapper.toDomain(entity.getPaciente()))
        .medicoId(entity.getMedicoId())
        .fechaRecepcion(entity.getFechaRecepcion())
        .estado(entity.getEstado())
        .usuarioCreacionId(entity.getUsuarioCreacionId())
        .total(entity.getTotal())
        .detalles(detalles)
        .build();
  }

  public OrdenEntity toEntity(Orden orden, PacienteEntity pacienteEntity) {
    if (orden == null)
      return null;

    OrdenEntity entity = OrdenEntity.builder()
        .id(orden.getId())
        .codigoOrden(orden.getCodigoOrden())
        .numeroOrden(orden.getNumeroOrden())
        .paciente(pacienteEntity)
        .medicoId(orden.getMedicoId())
        .fechaRecepcion(orden.getFechaRecepcion())
        .estado(orden.getEstado())
        .usuarioCreacionId(orden.getUsuarioCreacionId())
        .total(orden.getTotal())
        .build();

    if (orden.getDetalles() != null) {
      List<OrdenDetalleEntity> detallesEntity = orden.getDetalles().stream()
          .map(d -> toDetalleEntity(d, entity))
          .collect(Collectors.toList());
      entity.setDetalles(detallesEntity);
    }

    return entity;
  }

  private OrdenDetalle toDetalleDomain(OrdenDetalleEntity entity) {
    return OrdenDetalle.builder()
        .id(entity.getId())
        .ordenId(entity.getOrden() != null ? entity.getOrden().getId() : null)
        .examen(examenMapper.toDomain(entity.getExamen()))
        .precioCobrado(entity.getPrecioCobrado())
        .estado(entity.getEstado())
        .build();
  }

  private OrdenDetalleEntity toDetalleEntity(OrdenDetalle detalle, OrdenEntity ordenEntity) {
    ExamenEntity examenEntity = null;
    if (detalle.getExamen() != null) {
      examenEntity = examenMapper.toEntity(detalle.getExamen());
    }

    return OrdenDetalleEntity.builder()
        .id(detalle.getId())
        .orden(ordenEntity)
        .examen(examenEntity)
        .precioCobrado(detalle.getPrecioCobrado())
        .estado(detalle.getEstado())
        .build();
  }
}
