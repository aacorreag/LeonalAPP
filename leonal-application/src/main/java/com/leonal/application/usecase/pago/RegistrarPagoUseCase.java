package com.leonal.application.usecase.pago;

import com.leonal.application.dto.pago.CreatePagoRequest;
import com.leonal.application.dto.pago.PagoDto;
import com.leonal.domain.model.Factura;
import com.leonal.domain.model.Pago;
import com.leonal.domain.port.output.FacturaRepositoryPort;
import com.leonal.domain.port.output.PagoRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class RegistrarPagoUseCase {
    private final PagoRepositoryPort pagoRepository;
    private final FacturaRepositoryPort facturaRepository;

    public PagoDto execute(CreatePagoRequest request, UUID usuarioId) {
        // Validar que la factura exista
        Factura factura = facturaRepository.findById(request.getFacturaId())
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));

        // Validar que la factura no esté ya pagada
        if (factura.estaPagada()) {
            throw new IllegalArgumentException("La factura ya está pagada");
        }

        // Validar monto
        if (!request.getMonto().equals(factura.getTotal())) {
            throw new IllegalArgumentException("El monto debe ser igual al total de la factura");
        }

        // Crear pago
        Pago pago = Pago.builder()
                .facturaId(request.getFacturaId())
                .monto(request.getMonto())
                .formaPago(request.getFormaPago())
                .fecha(LocalDateTime.now())
                .referencia(request.getReferencia())
                .observaciones(request.getObservaciones())
                .usuarioRegistroId(usuarioId)
                .estado("REGISTRADO")
                .fechaCreacion(LocalDateTime.now())
                .build();

        // Validar integridad del pago
        if (!pago.esValido()) {
            throw new IllegalArgumentException("Datos de pago inválidos");
        }

        // Procesar pago
        pago.marcarComoProcessado();

        // Guardar pago
        Pago pagoGuardado = pagoRepository.save(pago);

        // Actualizar factura como pagada
        factura.marcarComoPagada();
        facturaRepository.save(factura);

        return mapToDto(pagoGuardado);
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
