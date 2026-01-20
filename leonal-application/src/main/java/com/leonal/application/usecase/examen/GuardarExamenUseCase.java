package com.leonal.application.usecase.examen;

import com.leonal.application.dto.examen.CreateExamenRequest;
import com.leonal.application.dto.examen.ExamenDto;
import com.leonal.domain.model.Examen;
import com.leonal.domain.port.output.ExamenRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class GuardarExamenUseCase {

  private final ExamenRepositoryPort examenRepositoryPort;

  // Transactional handling delegated to repository or service configuration
  public ExamenDto ejecutar(CreateExamenRequest request) {
    // Validate unique code if creating or if code changed (simplified check)
    if (request.getId() == null) {
      Optional<Examen> existing = examenRepositoryPort.findByCodigoInterno(request.getCodigoInterno());
      if (existing.isPresent()) {
        throw new IllegalArgumentException("El código interno ya existe.");
      }
    }

    Examen examen = Examen.builder()
        .id(request.getId() != null ? request.getId() : UUID.randomUUID())
        .codigoInterno(request.getCodigoInterno())
        .nombre(request.getNombre())
        .metodo(request.getMetodo())
        .tipoResultado(request.getTipoResultado())
        .unidadMedida(request.getUnidadMedida())
        .valoresReferencia(request.getValoresReferencia())
        .precio(request.getPrecio())
        .activo(request.isActivo())
        .build();

    Examen saved = examenRepositoryPort.save(examen);

    return ExamenDto.builder()
        .id(saved.getId())
        .codigoInterno(saved.getCodigoInterno())
        .nombre(saved.getNombre())
        .metodo(saved.getMetodo())
        .tipoResultado(saved.getTipoResultado())
        .unidadMedida(saved.getUnidadMedida())
        .valoresReferencia(saved.getValoresReferencia())
        .precio(saved.getPrecio())
        .activo(saved.isActivo())
        .build();
  }
}
