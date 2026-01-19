package com.leonal.domain.port.output;

import com.leonal.domain.model.Rol;
import java.util.List;
import java.util.Optional;

public interface RolRepositoryPort {
  List<Rol> findAll();

  Optional<Rol> findByName(String nombre);
}
