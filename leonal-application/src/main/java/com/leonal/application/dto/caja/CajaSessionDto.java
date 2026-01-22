package com.leonal.application.dto.caja;

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
public class CajaSessionDto {
    private UUID id;
    private UUID usuarioCajeroId;
    private String usuarioCajeroNombre;
    private LocalDate fecha;
    private LocalDateTime horaApertura;
    private LocalDateTime horaCierre;
    private BigDecimal montoInicial;
    private BigDecimal montoFinal;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private String estado;
    private String observaciones;
}
