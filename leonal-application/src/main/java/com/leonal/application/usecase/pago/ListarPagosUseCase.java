package com.leonal.application.usecase.pago;

import com.leonal.application.dto.pago.PagoDto;
import com.leonal.domain.model.Pago;
import com.leonal.domain.port.output.PagoRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListarPagosUseCase {
    private final PagoRepositoryPort pagoRepository;

    public List<PagoDto> execute() {
        return pagoRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PagoDto> executeByFactura(UUID facturaId) {
        return pagoRepository.findByFacturaId(facturaId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PagoDto> executeByEstado(String estado) {
        return pagoRepository.findByEstado(estado).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PagoDto mapToDto(Pago pago) {
        return PagoDto.builder()
                .id(pago.getId())
                .facturaId(pago.getFacturaId())
                .monto(pago.getMonto())
                .formaPago(pago.getFormaPago())
                .fecha(pago.getFecha())
                .referencia(pago.getReferencia())
                .observaciones(pago.getObservaciones())
                .estado(pago.getEstado())
                .fechaCreacion(pago.getFechaCreacion())
                .build();
    }
}
