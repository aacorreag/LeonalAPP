package com.leonal.domain.port.output;

import com.leonal.domain.model.Usuario;
import java.util.Optional;

public interface UsuarioRepositoryPort {
  Optional<Usuario> findByUsername(String username);
}
