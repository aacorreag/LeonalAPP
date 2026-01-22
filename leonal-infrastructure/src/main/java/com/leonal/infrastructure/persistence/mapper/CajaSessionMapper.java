package com.leonal.infrastructure.persistence.mapper;

import com.leonal.domain.model.CajaSession;
import com.leonal.infrastructure.persistence.entity.CajaSessionEntity;
import org.springframework.stereotype.Component;

@Component
public class CajaSessionMapper {
    public CajaSessionEntity toEntity(CajaSession cajaSession) {
        return CajaSessionEntity.builder()
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
                .fechaCreacion(cajaSession.getFechaCreacion())
                .build();
    }

    public CajaSession toDomain(CajaSessionEntity entity) {
        return CajaSession.builder()
                .id(entity.getId())
                .usuarioCajeroId(entity.getUsuarioCajeroId())
                .usuarioCajeroNombre(entity.getUsuarioCajeroNombre())
                .fecha(entity.getFecha())
                .horaApertura(entity.getHoraApertura())
                .horaCierre(entity.getHoraCierre())
                .montoInicial(entity.getMontoInicial())
                .montoFinal(entity.getMontoFinal())
                .totalIngresos(entity.getTotalIngresos())
                .totalEgresos(entity.getTotalEgresos())
                .estado(entity.getEstado())
                .observaciones(entity.getObservaciones())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}
