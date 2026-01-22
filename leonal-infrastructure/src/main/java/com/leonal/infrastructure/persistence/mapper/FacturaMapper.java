package com.leonal.infrastructure.persistence.mapper;

import com.leonal.domain.model.Factura;
import com.leonal.infrastructure.persistence.entity.FacturaEntity;
import org.springframework.stereotype.Component;

@Component
public class FacturaMapper {
    public FacturaEntity toEntity(Factura factura) {
        return FacturaEntity.builder()
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
                .usuarioCreacionId(factura.getUsuarioCreacionId())
                .usuarioModificacionId(factura.getUsuarioModificacionId())
                .fechaModificacion(factura.getFechaModificacion())
                .build();
    }

    public Factura toDomain(FacturaEntity entity) {
        return Factura.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .fechaEmision(entity.getFechaEmision())
                .fechaCreacion(entity.getFechaCreacion())
                .ordenId(entity.getOrdenId())
                .pacienteId(entity.getPacienteId())
                .pacienteNombre(entity.getPacienteNombre())
                .pacienteDocumento(entity.getPacienteDocumento())
                .subtotal(entity.getSubtotal())
                .descuento(entity.getDescuento())
                .impuesto(entity.getImpuesto())
                .total(entity.getTotal())
                .estado(entity.getEstado())
                .observaciones(entity.getObservaciones())
                .usuarioCreacionId(entity.getUsuarioCreacionId())
                .usuarioModificacionId(entity.getUsuarioModificacionId())
                .fechaModificacion(entity.getFechaModificacion())
                .build();
    }
}
