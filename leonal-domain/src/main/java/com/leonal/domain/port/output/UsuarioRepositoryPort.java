package com.leonal.domain.port.output;

import com.leonal.domain.model.Usuario;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositoryPort {
  Optional<Usuario> findByUsername(String username);

  List<Usuario> findAll();

  Usuario save(Usuario usuario);

  void delete(UUID id); // Logical delete preferred usually, but simple delete for now

  Optional<Usuario> findById(UUID id);
}
