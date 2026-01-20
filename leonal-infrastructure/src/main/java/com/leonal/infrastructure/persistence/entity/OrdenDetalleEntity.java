package com.leonal.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orden_detalles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDetalleEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orden_id", nullable = false)
  private OrdenEntity orden;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "examen_id", nullable = false)
  private ExamenEntity examen;

  @Column(name = "precio_cobrado", nullable = false)
  private BigDecimal precioCobrado;

  @Column(name = "estado")
  private String estado;

  @OneToOne(mappedBy = "ordenDetalle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private ResultadoEntity resultado;
}
