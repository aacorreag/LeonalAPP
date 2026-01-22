package com.leonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    private UUID id;
    private UUID facturaId;
    private BigDecimal monto;
    private FormaPago formaPago;
    private LocalDateTime fecha;
    private String referencia; // Número de cheque, referencia de transferencia, etc.
    private String observaciones;
    private UUID usuarioRegistroId;
    private String estado; // REGISTRADO, PROCESADO, RECHAZADO
    private LocalDateTime fechaCreacion;

    public boolean esValido() {
        return monto != null && monto.compareTo(BigDecimal.ZERO) > 0 &&
               formaPago != null && facturaId != null;
    }

    public void marcarComoProcessado() {
        this.estado = "PROCESADO";
    }

    public void marcarComoRechazado() {
        this.estado = "RECHAZADO";
    }
}
