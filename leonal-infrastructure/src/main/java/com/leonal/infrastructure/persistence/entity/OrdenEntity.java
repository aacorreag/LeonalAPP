package com.leonal.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ordenes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "numero_orden")
  private Integer numeroOrden;

  @Column(name = "codigo_orden", unique = true, nullable = false)
  private String codigoOrden;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "paciente_id", nullable = false)
  private PacienteEntity paciente;

  @Column(name = "medico_id")
  private UUID medicoId;

  @Column(name = "fecha_recepcion")
  private LocalDateTime fechaRecepcion;

  @Column(name = "estado")
  private String estado;

  @Column(name = "usuario_creacion_id")
  private UUID usuarioCreacionId;

  @Column(name = "total")
  private BigDecimal total;

  @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<OrdenDetalleEntity> detalles = new ArrayList<>();
}
