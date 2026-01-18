package com.leonal.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resultado {
    private UUID id;
    private UUID ordenDetalleId;
    private String valor;
    private String observacionInterna;
    private String observacionReporte;
    private boolean esPatologico;
    private LocalDateTime fechaResultado;
    private UUID usuarioResultadoId;
    
    private LocalDateTime fechaValidacion;
    private UUID usuarioValidacionId;
    
    @Builder.Default
    private List<ResultadoVersion> versiones = new ArrayList<>();

    public void agregarVersion(String valorAnterior, UUID usuarioId, String motivo) {
        if (this.versiones == null) {
            this.versiones = new ArrayList<>();
        }
        this.versiones.add(ResultadoVersion.builder()
                .resultadoId(this.id)
                .valorAnterior(valorAnterior)
                .usuarioModificoId(usuarioId)
                .motivoCambio(motivo)
                .fechaCambio(LocalDateTime.now())
                .build());
    }
}
