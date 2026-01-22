package com.leonal.application.usecase.factura;

import com.leonal.application.dto.factura.FacturaDto;
import com.leonal.domain.model.Factura;
import com.leonal.domain.port.output.FacturaRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListarFacturasUseCase {
    private final FacturaRepositoryPort facturaRepository;

    public List<FacturaDto> execute() {
        return facturaRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<FacturaDto> executeByPaciente(UUID pacienteId) {
        return facturaRepository.findByPacienteId(pacienteId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<FacturaDto> executeByEstado(String estado) {
        return facturaRepository.findByEstado(estado).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private FacturaDto mapToDto(Factura factura) {
        return FacturaDto.builder()
                .id(factura.getId())
                .numero(factura.getNumero())
                .fechaEmision(factura.getFechaEmision())
                .fechaCreacion(factura.getFechaCreacion())
                .ordenId(factura.getOrdenId())
                .pacienteId(factura.getPacienteId())
                .pacienteNombre(factura.getPacienteNombre())
                .pacienteDocumento(factura.getPacienteDocumento())
                .subtotal(factura.getSubtotal())
                .descuento(factura.getDescuento())
                .impuesto(factura.getImpuesto())
                .total(factura.getTotal())
                .estado(factura.getEstado())
                .observaciones(factura.getObservaciones())
                .build();
    }
}
