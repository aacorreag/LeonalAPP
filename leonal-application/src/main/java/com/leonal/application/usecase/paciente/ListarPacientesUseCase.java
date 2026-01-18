package com.leonal.application.usecase.paciente;

import com.leonal.domain.model.Paciente;
import com.leonal.domain.port.output.PacienteRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListarPacientesUseCase {

  private final PacienteRepositoryPort pacienteRepository;

  public List<Paciente> execute() {
    return pacienteRepository.findAll();
  }
}
