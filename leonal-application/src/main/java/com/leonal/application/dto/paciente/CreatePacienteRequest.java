package com.leonal.application.dto.paciente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePacienteRequest {
  private String tipoDocumento;
  private String numeroDocumento;
  private String nombre;
  private LocalDate fechaNacimiento;
  private String sexo;
  private String direccion;
  private String telefono;
  private String email;
}
