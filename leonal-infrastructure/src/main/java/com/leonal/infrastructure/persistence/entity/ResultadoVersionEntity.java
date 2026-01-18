package com.leonal.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resultado_versiones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoVersionEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resultado_id")
  private ResultadoEntity resultado;

  @Column(name = "valor_anterior", length = 500)
  private String valorAnterior;

  @Column(name = "motivo_cambio", columnDefinition = "TEXT")
  private String motivoCambio;

  @Column(name = "usuario_modifico_id")
  private UUID usuarioModificoId;

  @Column(name = "fecha_cambio")
  private LocalDateTime fechaCambio;
}
