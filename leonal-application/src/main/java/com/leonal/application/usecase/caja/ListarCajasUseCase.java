package com.leonal.application.usecase.caja;

import com.leonal.application.dto.caja.CajaSessionDto;
import com.leonal.domain.model.CajaSession;
import com.leonal.domain.port.output.CajaRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListarCajasUseCase {
    private final CajaRepositoryPort cajaRepository;

    public List<CajaSessionDto> execute() {
        return cajaRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CajaSessionDto> executeByEstado(String estado) {
        return cajaRepository.findByEstado(estado).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CajaSessionDto> executeByCajero(UUID usuarioCajeroId) {
        return cajaRepository.findByCajero(usuarioCajeroId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
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
