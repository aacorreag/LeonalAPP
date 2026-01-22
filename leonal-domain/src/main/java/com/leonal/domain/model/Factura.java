package com.leonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Factura {
    private UUID id;
    private String numero;
    private LocalDate fechaEmision;
    private LocalDateTime fechaCreacion;
    private UUID ordenId;
    private UUID pacienteId;
    private String pacienteNombre;
    private String pacienteDocumento;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal impuesto;
    private BigDecimal total;
    private String estado; // BORRADOR, EMITIDA, PAGADA, ANULADA
    private String observaciones;
    private UUID usuarioCreacionId;
    private UUID usuarioModificacionId;
    private LocalDateTime fechaModificacion;

    public void marcarComoPagada() {
        this.estado = "PAGADA";
        this.fechaModificacion = LocalDateTime.now();
    }

    public void anular() {
        this.estado = "ANULADA";
        this.fechaModificacion = LocalDateTime.now();
    }

    public boolean puedeSerModificada() {
        return "BORRADOR".equals(this.estado);
    }

    public boolean estaPagada() {
        return "PAGADA".equals(this.estado);
    }
}
