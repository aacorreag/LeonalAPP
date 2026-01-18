package com.leonal.application.usecase.paciente;

import com.leonal.application.dto.paciente.CreatePacienteRequest;
import com.leonal.domain.model.Paciente;
import com.leonal.domain.port.output.PacienteRepositoryPort;
import lombok.RequiredArgsConstructor;

// No Spring annotations here!
@RequiredArgsConstructor
public class CrearPacienteUseCase {

  private final PacienteRepositoryPort pacienteRepository;

  public Paciente execute(CreatePacienteRequest request) {
    // 1. Validate Business Rules (e.g. unique document check logic could be here or
    // in domain service)
    if (pacienteRepository.findByDocumento(request.getTipoDocumento(), request.getNumeroDocumento()).isPresent()) {
      throw new IllegalArgumentException("Ya existe un paciente con ese documento");
    }

    // 2. Map DTO to Domain Entity
    Paciente paciente = Paciente.builder()
        .tipoDocumento(request.getTipoDocumento())
        .numeroDocumento(request.getNumeroDocumento())
        .nombre(request.getNombre())
        .fechaNacimiento(request.getFechaNacimiento())
        .sexo(request.getSexo())
        .direccion(request.getDireccion())
        .telefono(request.getTelefono())
        .email(request.getEmail())
        .build();

    // 3. Persist
    return pacienteRepository.save(paciente);
  }
}
