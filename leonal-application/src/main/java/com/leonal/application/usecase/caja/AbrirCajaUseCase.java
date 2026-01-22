package com.leonal.application.usecase.caja;

import com.leonal.application.dto.caja.CajaSessionDto;
import com.leonal.domain.model.CajaSession;
import com.leonal.domain.port.output.CajaRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class AbrirCajaUseCase {
    private final CajaRepositoryPort cajaRepository;

    public CajaSessionDto execute(UUID usuarioCajeroId, String usuarioCajeroNombre, BigDecimal montoInicial) {
        // Validar que no exista caja abierta para el usuario
        Optional<CajaSession> cajaAbierta = cajaRepository.findAbiertaByCajero(usuarioCajeroId);
        if (cajaAbierta.isPresent()) {
            throw new IllegalArgumentException("Ya existe una caja abierta para este cajero");
        }

        // Validar monto inicial
        if (montoInicial == null || montoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto inicial debe ser mayor o igual a 0");
        }

        // Crear nueva sesión de caja
        CajaSession cajaSession = CajaSession.builder()
                .id(UUID.randomUUID())
                .usuarioCajeroId(usuarioCajeroId)
                .usuarioCajeroNombre(usuarioCajeroNombre)
                .fecha(LocalDate.now())
                .horaApertura(LocalDateTime.now())
                .montoInicial(montoInicial)
                .totalIngresos(BigDecimal.ZERO)
                .totalEgresos(BigDecimal.ZERO)
                .estado("ABIERTA")
                .fechaCreacion(LocalDateTime.now())
                .build();

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
