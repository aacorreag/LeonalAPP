package com.leonal.application.dto.orden;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrdenRequest {
  // Si pacienteId es null, se usa nuevoPaciente para crear uno
  private UUID pacienteId;

  // Datos del nuevo paciente (si no existe)
  private NuevoPacienteData nuevoPaciente;

  // Lista de exámenes a incluir en la orden
  private List<UUID> examenIds;

  // Médico solicitante (opcional)
  private UUID medicoId;

  // Usuario que crea la orden
  private UUID usuarioCreacionId;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class NuevoPacienteData {
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String direccion;
    private String telefono;
    private String email;
  }
}
