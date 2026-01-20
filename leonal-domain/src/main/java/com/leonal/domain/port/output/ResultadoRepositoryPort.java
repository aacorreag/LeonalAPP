package com.leonal.domain.port.output;

import com.leonal.domain.model.Resultado;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResultadoRepositoryPort {
  Resultado save(Resultado resultado);

  Optional<Resultado> findById(UUID id);

  Optional<Resultado> findByOrdenDetalleId(UUID ordenDetalleId);

  List<Resultado> findByOrdenId(UUID ordenId);
}
