package com.leonal.application.usecase.orden;

import com.leonal.application.dto.orden.OrdenDetalleDto;
import com.leonal.application.dto.orden.OrdenDto;
import com.leonal.domain.model.Orden;
import com.leonal.domain.model.OrdenDetalle;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use case para obtener el detalle completo de una orden por su ID
 */
@RequiredArgsConstructor
public class ObtenerOrdenPorIdUseCase {
    private final OrdenRepositoryPort ordenRepository;

    public OrdenDto execute(UUID ordenId) {
        Orden orden = ordenRepository.findById(ordenId)
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + ordenId));

        return mapToDto(orden);
    }

    private OrdenDto mapToDto(Orden orden) {
        List<OrdenDetalleDto> detallesDto = orden.getDetalles() != null
            ? orden.getDetalles().stream().map(this::mapDetalleToDto).collect(Collectors.toList())
            : List.of();

        return OrdenDto.builder()
            .id(orden.getId())
            .codigoOrden(orden.getCodigoOrden())
            .pacienteId(orden.getPaciente() != null ? orden.getPaciente().getId() : null)
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

    private OrdenDetalleDto mapDetalleToDto(OrdenDetalle detalle) {
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
