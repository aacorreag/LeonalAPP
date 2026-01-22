package com.leonal.infrastructure.persistence.mapper;

import com.leonal.domain.model.Pago;
import com.leonal.domain.model.FormaPago;
import com.leonal.infrastructure.persistence.entity.PagoEntity;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {
    public PagoEntity toEntity(Pago pago) {
        return PagoEntity.builder()
                .id(pago.getId())
                .facturaId(pago.getFacturaId())
                .monto(pago.getMonto())
                .formaPago(pago.getFormaPago().name())
                .fecha(pago.getFecha())
                .referencia(pago.getReferencia())
                .observaciones(pago.getObservaciones())
                .usuarioRegistroId(pago.getUsuarioRegistroId())
                .estado(pago.getEstado())
                .fechaCreacion(pago.getFechaCreacion())
                .build();
    }

    public Pago toDomain(PagoEntity entity) {
        return Pago.builder()
                .id(entity.getId())
                .facturaId(entity.getFacturaId())
                .monto(entity.getMonto())
                .formaPago(FormaPago.valueOf(entity.getFormaPago()))
                .fecha(entity.getFecha())
                .referencia(entity.getReferencia())
                .observaciones(entity.getObservaciones())
                .usuarioRegistroId(entity.getUsuarioRegistroId())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}
