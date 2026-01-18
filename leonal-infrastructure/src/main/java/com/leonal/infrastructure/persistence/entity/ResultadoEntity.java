package com.leonal.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "resultados")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(name = "orden_detalle_id", nullable = false, unique = true)
  private UUID ordenDetalleId;

  @Column(length = 500)
  private String valor;

  @Column(name = "observacion_interna", columnDefinition = "TEXT")
  private String observacionInterna;

  @Column(name = "observacion_reporte", columnDefinition = "TEXT")
  private String observacionReporte;

  @Column(name = "es_patologico")
  private boolean esPatologico;

  @Column(name = "fecha_resultado")
  private LocalDateTime fechaResultado;

  @Column(name = "usuario_resultado_id")
  private UUID usuarioResultadoId;

  @Column(name = "fecha_validacion")
  private LocalDateTime fechaValidacion;

  @Column(name = "usuario_validacion_id")
  private UUID usuarioValidacionId;

  @OneToMany(mappedBy = "resultado", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ResultadoVersionEntity> versiones = new ArrayList<>();
}
