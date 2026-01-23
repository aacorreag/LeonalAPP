package com.leonal.application.usecase.orden;

import com.leonal.domain.model.Orden;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Use case para actualizar el estado de una orden
 * Estados válidos: PENDIENTE -> PROCESO -> VALIDADO -> ENTREGADO
 */
@RequiredArgsConstructor
public class ActualizarEstadoOrdenUseCase {
    private final OrdenRepositoryPort ordenRepository;

    public void execute(UUID ordenId, String nuevoEstado) {
        Orden orden = ordenRepository.findById(ordenId)
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + ordenId));

        validarTransicionEstado(orden.getEstado(), nuevoEstado);
        orden.setEstado(nuevoEstado);
        ordenRepository.save(orden);
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
            case "PENDIENTE" -> "PROCESO".equals(nuevoEstado);
            case "PROCESO" -> "VALIDADO".equals(nuevoEstado);
            case "VALIDADO" -> "ENTREGADO".equals(nuevoEstado);
            case "ENTREGADO" -> false; // Estado final
            default -> false;
        };
    }
}
