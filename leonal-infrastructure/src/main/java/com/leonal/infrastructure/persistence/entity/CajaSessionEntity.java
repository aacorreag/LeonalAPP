package com.leonal.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "caja_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaSessionEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "usuario_cajero_id", nullable = false)
    private UUID usuarioCajeroId;

    @Column(name = "usuario_cajero_nombre", nullable = false)
    private String usuarioCajeroNombre;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_apertura", nullable = false)
    private LocalDateTime horaApertura;

    @Column(name = "hora_cierre")
    private LocalDateTime horaCierre;

    @Column(name = "monto_inicial", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoInicial;

    @Column(name = "monto_final", precision = 10, scale = 2)
    private BigDecimal montoFinal;

    @Column(name = "total_ingresos", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalIngresos;

    @Column(name = "total_egresos", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalEgresos;

    @Column(name = "estado", nullable = false)
    private String estado; // ABIERTA, CERRADA

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

}
