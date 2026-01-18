package com.leonal.domain.port.output;

import java.util.UUID;

public interface AuditPort {
  void logEvent(UUID usuarioId, String accion, String entidad, UUID entidadId, String detalle);
}
