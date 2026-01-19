package com.leonal.application.usecase.orden;

import com.leonal.application.dto.orden.OrdenDetalleDto;
import com.leonal.application.dto.orden.OrdenDto;
import com.leonal.domain.model.Orden;
import com.leonal.domain.model.OrdenDetalle;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListarOrdenesUseCase {

  private final OrdenRepositoryPort ordenRepository;

  public List<OrdenDto> ejecutar() {
    return ordenRepository.findAll().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  private OrdenDto toDto(Orden orden) {
    List<OrdenDetalleDto> detallesDto = orden.getDetalles() != null
        ? orden.getDetalles().stream().map(this::toDetalleDto).collect(Collectors.toList())
        : List.of();

    return OrdenDto.builder()
        .id(orden.getId())
        .codigoOrden(orden.getCodigoOrden())
        .pacienteNombre(orden.getPaciente() != null ? orden.getPaciente().getNombre() : "")
        .pacienteDocumento(orden.getPaciente() != null
            ? orden.getPaciente().getTipoDocumento() + " " + orden.getPaciente().getNumeroDocumento()
            : "")
        .fechaRecepcion(orden.getFechaRecepcion())
        .estado(orden.getEstado())
        .total(orden.getTotal())
        .itemCount(detallesDto.size())
        .detalles(detallesDto)
        .build();
  }

  private OrdenDetalleDto toDetalleDto(OrdenDetalle detalle) {
    return OrdenDetalleDto.builder()
        .id(detalle.getId())
        .examenId(detalle.getExamen() != null ? detalle.getExamen().getId() : null)
        .examenCodigo(detalle.getExamen() != null ? detalle.getExamen().getCodigoInterno() : "")
        .examenNombre(detalle.getExamen() != null ? detalle.getExamen().getNombre() : "")
        .precioCobrado(detalle.getPrecioCobrado())
        .estado(detalle.getEstado())
        .build();
  }
}
