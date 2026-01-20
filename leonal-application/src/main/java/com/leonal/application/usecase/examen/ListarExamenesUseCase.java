package com.leonal.application.usecase.examen;

import com.leonal.application.dto.examen.ExamenDto;
import com.leonal.domain.model.Examen;
import com.leonal.domain.port.output.ExamenRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListarExamenesUseCase {

  private final ExamenRepositoryPort examenRepositoryPort;

  public List<ExamenDto> ejecutar() {
    return examenRepositoryPort.findAll().stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  private ExamenDto mapToDto(Examen examen) {
    return ExamenDto.builder()
        .id(examen.getId())
        .codigoInterno(examen.getCodigoInterno())
        .nombre(examen.getNombre())
        .metodo(examen.getMetodo())
        .tipoResultado(examen.getTipoResultado())
        .unidadMedida(examen.getUnidadMedida())
        .valoresReferencia(examen.getValoresReferencia())
        .precio(examen.getPrecio())
        .activo(examen.isActivo())
        .build();
  }
}
