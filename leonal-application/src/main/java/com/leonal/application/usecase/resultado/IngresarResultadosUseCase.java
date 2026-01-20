package com.leonal.application.usecase.resultado;

import com.leonal.application.dto.resultado.ActualizarResultadosRequest;
import com.leonal.application.dto.resultado.ItemResultadoRequest;
import com.leonal.domain.model.Orden;
import com.leonal.domain.model.Resultado;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import com.leonal.domain.port.output.ResultadoRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class IngresarResultadosUseCase {

  private final ResultadoRepositoryPort resultadoRepository;
  private final OrdenRepositoryPort ordenRepository;

  public void execute(ActualizarResultadosRequest request, UUID usuarioId) {
    Orden orden = ordenRepository.findById(request.getOrdenId())
        .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

    for (ItemResultadoRequest item : request.getResultados()) {
      Resultado resultado = resultadoRepository.findByOrdenDetalleId(item.getOrdenDetalleId())
          .orElse(Resultado.builder()
              .ordenDetalleId(item.getOrdenDetalleId())
              .build());

      String valorAnterior = resultado.getValor();

      // Si el valor cambia y ya existía, agregamos a versiones
      if (resultado.getId() != null && valorAnterior != null && !valorAnterior.equals(item.getValor())) {
        resultado.agregarVersion(valorAnterior, usuarioId, "Actualización de resultado");
      }

      resultado.setValor(item.getValor());
      resultado.setObservacionReporte(item.getObservacionReporte());
      resultado.setEsPatologico(item.isEsPatologico());
      resultado.setFechaResultado(LocalDateTime.now());
      resultado.setUsuarioResultadoId(usuarioId);

      resultadoRepository.save(resultado);
    }

    // Si la orden estaba PENDIENTE, pasa a PROCESO
    if ("PENDIENTE".equals(orden.getEstado())) {
      orden.setEstado("PROCESO");
      ordenRepository.save(orden);
    }
  }
}
