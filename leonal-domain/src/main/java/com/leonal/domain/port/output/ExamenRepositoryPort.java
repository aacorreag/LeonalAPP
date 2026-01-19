package com.leonal.domain.port.output;

import com.leonal.domain.model.Examen;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExamenRepositoryPort {
  List<Examen> findAll();

  Optional<Examen> findById(UUID id);

  Examen save(Examen examen);

  Optional<Examen> findByCodigoInterno(String codigoInterno);
}
