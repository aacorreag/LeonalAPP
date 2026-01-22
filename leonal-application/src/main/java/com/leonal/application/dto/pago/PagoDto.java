package com.leonal.application.dto.pago;

import com.leonal.domain.model.FormaPago;
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
public class PagoDto {
    private UUID id;
    private UUID facturaId;
    private BigDecimal monto;
    private FormaPago formaPago;
    private LocalDateTime fecha;
    private String referencia;
    private String observaciones;
    private String estado;
    private LocalDateTime fechaCreacion;
}
