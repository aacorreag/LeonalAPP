package com.leonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDetalle {
    private UUID id;
    private UUID ordenId;
    private Examen examen;
    private BigDecimal precioCobrado;
    private String estado;
    private Resultado resultado;
}
