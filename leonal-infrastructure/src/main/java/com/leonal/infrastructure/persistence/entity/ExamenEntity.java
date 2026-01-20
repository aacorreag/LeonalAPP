package com.leonal.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "examenes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamenEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(name = "codigo_interno", nullable = false, unique = true)
  private String codigoInterno;

  @Column(nullable = false)
  private String nombre;

  private String metodo;

  @Column(name = "tipo_resultado")
  private String tipoResultado;

  @Column(name = "unidad_medida")
  private String unidadMedida;

  @Column(name = "valores_referencia")
  private String valoresReferencia;

  @Column(nullable = false)
  private BigDecimal precio;

  @Column(nullable = false)
  private boolean activo;
}
