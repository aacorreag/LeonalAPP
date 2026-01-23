package com.leonal.application.usecase.factura;

import com.leonal.domain.model.Factura;
import com.leonal.domain.port.output.FacturaRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Use case para actualizar el estado de una factura
 * Estados válidos: BORRADOR -> EMITIDA -> PAGADA / ANULADA
 */
@RequiredArgsConstructor
public class ActualizarEstadoFacturaUseCase {
    private final FacturaRepositoryPort facturaRepository;

    public void execute(UUID facturaId, String nuevoEstado) {
        Factura factura = facturaRepository.findById(facturaId)
            .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada: " + facturaId));

        validarTransicionEstado(factura.getEstado(), nuevoEstado);
        factura.setEstado(nuevoEstado);
        factura.setFechaModificacion(LocalDateTime.now());
        facturaRepository.save(factura);
    }

    private void validarTransicionEstado(String estadoActual, String nuevoEstado) {
        if (!esTransicionValida(estadoActual, nuevoEstado)) {
            throw new IllegalArgumentException(
                String.format("Transición inválida de estado: %s -> %s", estadoActual, nuevoEstado)
            );
        }
    }

    private boolean esTransicionValida(String estadoActual, String nuevoEstado) {
        return switch (estadoActual) {
            case "BORRADOR" -> "EMITIDA".equals(nuevoEstado) || "ANULADA".equals(nuevoEstado);
            case "EMITIDA" -> "PAGADA".equals(nuevoEstado) || "ANULADA".equals(nuevoEstado);
            case "PAGADA" -> "ANULADA".equals(nuevoEstado);
            case "ANULADA" -> false; // Estado final
            default -> false;
        };
    }
}
