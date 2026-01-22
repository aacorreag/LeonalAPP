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
public class CajaSession {
    private UUID id;
    private UUID usuarioCajeroId;
    private String usuarioCajeroNombre;
    private LocalDate fecha;
    private LocalDateTime horaApertura;
    private LocalDateTime horaCierre;
    private BigDecimal montoInicial;
    private BigDecimal montoFinal;
    private BigDecimal totalIngresos; // Suma de todos los pagos
    private BigDecimal totalEgresos;  // Suma de retiros/gastos
    private String estado; // ABIERTA, CERRADA
    private String observaciones;
    private LocalDateTime fechaCreacion;

    public BigDecimal calcularDiferencia() {
        return totalIngresos.add(montoInicial).subtract(totalEgresos);
    }

    public boolean puedeSerCerrada() {
        return "ABIERTA".equals(this.estado);
    }

    public void cerrar(BigDecimal montoFinal) {
        this.montoFinal = montoFinal;
        this.horaCierre = LocalDateTime.now();
        this.estado = "CERRADA";
    }
}
