package com.leonal.domain.port.output;

import com.leonal.domain.model.Paciente;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface PacienteRepositoryPort {
  Paciente save(Paciente paciente);

  Optional<Paciente> findById(UUID id);

  Optional<Paciente> findByDocumento(String tipoDocumento, String numeroDocumento);

  List<Paciente> findAll();

  List<Paciente> search(String query);

  void deleteById(UUID id);
}
