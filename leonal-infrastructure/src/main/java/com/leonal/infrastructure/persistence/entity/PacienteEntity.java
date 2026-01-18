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

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pacientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(name = "tipo_documento", nullable = false)
  private String tipoDocumento;

  @Column(name = "numero_documento", nullable = false)
  private String numeroDocumento;

  @Column(nullable = false)
  private String nombre;

  @Column(name = "fecha_nacimiento", nullable = false)
  private LocalDate fechaNacimiento;

  private String sexo;
  private String direccion;
  private String telefono;
  private String email;
}
