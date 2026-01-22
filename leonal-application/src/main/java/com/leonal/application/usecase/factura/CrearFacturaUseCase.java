package com.leonal.application.usecase.factura;

import com.leonal.application.dto.factura.CreateFacturaRequest;
import com.leonal.application.dto.factura.FacturaDto;
import com.leonal.domain.model.Factura;
import com.leonal.domain.port.output.FacturaRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class CrearFacturaUseCase {
    private final FacturaRepositoryPort facturaRepository;

    public FacturaDto execute(CreateFacturaRequest request, UUID pacienteId, String pacienteNombre, String pacienteDocumento, UUID usuarioId) {
        // Validaciones de negocio
        if (request.getOrdenId() == null) {
            throw new IllegalArgumentException("La orden es obligatoria");
        }

        if (request.getSubtotal() == null || request.getSubtotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El subtotal debe ser mayor a 0");
        }

        // Generar número de factura
        String numeroFactura = facturaRepository.generarProxNumeroFactura();

        // Crear factura
        Factura factura = Factura.builder()
                .numero(numeroFactura)
                .fechaEmision(LocalDate.now())
                .fechaCreacion(LocalDateTime.now())
                .ordenId(request.getOrdenId())
                .pacienteId(pacienteId)
                .pacienteNombre(pacienteNombre)
                .pacienteDocumento(pacienteDocumento)
                .subtotal(request.getSubtotal())
                .descuento(request.getDescuento() != null ? request.getDescuento() : BigDecimal.ZERO)
                .impuesto(request.getImpuesto() != null ? request.getImpuesto() : BigDecimal.ZERO)
                .total(calcularTotal(request.getSubtotal(), request.getDescuento(), request.getImpuesto()))
                .estado("EMITIDA")
                .observaciones(request.getObservaciones())
                .usuarioCreacionId(usuarioId)
                .build();

        Factura facturaGuardada = facturaRepository.save(factura);
        return mapToDto(facturaGuardada);
    }

    private BigDecimal calcularTotal(BigDecimal subtotal, BigDecimal descuento, BigDecimal impuesto) {
        BigDecimal desc = descuento != null ? descuento : BigDecimal.ZERO;
        BigDecimal imp = impuesto != null ? impuesto : BigDecimal.ZERO;
        return subtotal.subtract(desc).add(imp);
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
