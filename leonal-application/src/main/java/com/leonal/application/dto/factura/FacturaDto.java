package com.leonal.application.dto.factura;

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
public class FacturaDto {
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
    private String estado;
    private String observaciones;
}
