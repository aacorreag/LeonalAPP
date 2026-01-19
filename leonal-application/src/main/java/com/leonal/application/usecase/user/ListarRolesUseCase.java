package com.leonal.application.usecase.user;

import com.leonal.domain.model.Rol;
import com.leonal.domain.port.output.RolRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ListarRolesUseCase {
  private final RolRepositoryPort rolRepository;

  public List<Rol> execute() {
    return rolRepository.findAll();
  }
}
