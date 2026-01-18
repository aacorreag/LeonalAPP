package com.leonal.infrastructure.persistence.mapper;

import com.leonal.domain.model.Paciente;
import com.leonal.infrastructure.persistence.entity.PacienteEntity;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

  public Paciente toDomain(PacienteEntity entity) {
    if (entity == null)
      return null;
    return Paciente.builder()
        .id(entity.getId())
        .tipoDocumento(entity.getTipoDocumento())
        .numeroDocumento(entity.getNumeroDocumento())
        .nombre(entity.getNombre())
        .fechaNacimiento(entity.getFechaNacimiento())
        .sexo(entity.getSexo())
        .direccion(entity.getDireccion())
        .telefono(entity.getTelefono())
        .email(entity.getEmail())
        .build();
  }

  public PacienteEntity toEntity(Paciente domain) {
    if (domain == null)
      return null;
    return PacienteEntity.builder()
        .id(domain.getId())
        .tipoDocumento(domain.getTipoDocumento())
        .numeroDocumento(domain.getNumeroDocumento())
        .nombre(domain.getNombre())
        .fechaNacimiento(domain.getFechaNacimiento())
        .sexo(domain.getSexo())
        .direccion(domain.getDireccion())
        .telefono(domain.getTelefono())
        .email(domain.getEmail())
        .build();
  }
}
