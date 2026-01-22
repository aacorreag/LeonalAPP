package com.leonal.application.usecase.caja;

import com.leonal.application.dto.caja.CajaSessionDto;
import com.leonal.domain.model.CajaSession;
import com.leonal.domain.port.output.CajaRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class CerrarCajaUseCase {
    private final CajaRepositoryPort cajaRepository;

    public CajaSessionDto execute(UUID cajaSessionId, BigDecimal montoFinal, String observaciones) {
        // Obtener caja
        CajaSession cajaSession = cajaRepository.findById(cajaSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Sesión de caja no encontrada"));

        // Validar que esté abierta
        if (!cajaSession.puedeSerCerrada()) {
            throw new IllegalArgumentException("La caja no está abierta");
        }

        // Validar monto final
        if (montoFinal == null || montoFinal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto final debe ser mayor o igual a 0");
        }

        // Cerrar caja
        cajaSession.cerrar(montoFinal);
        cajaSession.setObservaciones(observaciones);

        CajaSession cajaSaved = cajaRepository.save(cajaSession);
        return mapToDto(cajaSaved);
    }

    private CajaSessionDto mapToDto(CajaSession cajaSession) {
        return CajaSessionDto.builder()
                .id(cajaSession.getId())
                .usuarioCajeroId(cajaSession.getUsuarioCajeroId())
                .usuarioCajeroNombre(cajaSession.getUsuarioCajeroNombre())
                .fecha(cajaSession.getFecha())
                .horaApertura(cajaSession.getHoraApertura())
                .horaCierre(cajaSession.getHoraCierre())
                .montoInicial(cajaSession.getMontoInicial())
                .montoFinal(cajaSession.getMontoFinal())
                .totalIngresos(cajaSession.getTotalIngresos())
                .totalEgresos(cajaSession.getTotalEgresos())
                .estado(cajaSession.getEstado())
                .observaciones(cajaSession.getObservaciones())
                .build();
    }
}
