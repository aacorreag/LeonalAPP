package com.leonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoVersion {
    private UUID id;
    private UUID resultadoId;
    private String valorAnterior;
    private String motivoCambio;
    private UUID usuarioModificoId;
    private LocalDateTime fechaCambio;
}
