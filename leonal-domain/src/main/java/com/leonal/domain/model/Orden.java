package com.leonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orden {
    private UUID id;
    private String codigoOrden;
    private Integer numeroOrden;
    private Paciente paciente;
    private UUID medicoId;
    private LocalDateTime fechaRecepcion;
    private String estado;
    private UUID usuarioCreacionId;
    private BigDecimal total;

    private List<OrdenDetalle> detalles;
}
