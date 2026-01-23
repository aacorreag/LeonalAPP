package com.leonal.application.usecase.caja;

import com.leonal.domain.model.CajaSession;
import com.leonal.domain.port.output.CajaRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Use case para actualizar los totales de ingresos de una sesión de caja abierta
 */
@RequiredArgsConstructor
public class ActualizarTotalCajaUseCase {
    private final CajaRepositoryPort cajaRepository;

    public void execute(UUID cajaSessionId, BigDecimal montoIngreso) {
        CajaSession caja = cajaRepository.findById(cajaSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Caja no encontrada: " + cajaSessionId));

        if (!"ABIERTA".equals(caja.getEstado())) {
            throw new IllegalArgumentException("La caja no está abierta");
        }

        // Sumar el nuevo ingreso al total de ingresos
        BigDecimal nuevoTotal = caja.getTotalIngresos().add(montoIngreso);
        caja.setTotalIngresos(nuevoTotal);

        cajaRepository.save(caja);
    }
}
