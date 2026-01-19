package com.leonal.application.usecase.orden;

import com.leonal.application.dto.orden.CreateOrdenRequest;
import com.leonal.application.dto.orden.OrdenDetalleDto;
import com.leonal.application.dto.orden.OrdenDto;
import com.leonal.domain.model.Examen;
import com.leonal.domain.model.Orden;
import com.leonal.domain.model.OrdenDetalle;
import com.leonal.domain.model.Paciente;
import com.leonal.domain.port.output.ExamenRepositoryPort;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import com.leonal.domain.port.output.PacienteRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CrearOrdenUseCase {

  private final OrdenRepositoryPort ordenRepository;
  private final PacienteRepositoryPort pacienteRepository;
  private final ExamenRepositoryPort examenRepository;

  public OrdenDto ejecutar(CreateOrdenRequest request) {
    // 1. Resolver paciente (existente o nuevo)
    Paciente paciente = resolverPaciente(request);

    // 2. Recuperar exámenes seleccionados
    List<Examen> examenes = request.getExamenIds().stream()
        .map(id -> examenRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Examen no encontrado: " + id)))
        .collect(Collectors.toList());

    if (examenes.isEmpty()) {
      throw new IllegalArgumentException("Debe seleccionar al menos un examen");
    }

    // 3. Generar código de orden
    String codigoOrden = generarCodigoOrden();

    // 4. Calcular total
    BigDecimal total = examenes.stream()
        .map(Examen::getPrecio)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    // 5. Crear detalles
    List<OrdenDetalle> detalles = new ArrayList<>();
    for (Examen examen : examenes) {
      detalles.add(OrdenDetalle.builder()
          .examen(examen)
          .precioCobrado(examen.getPrecio())
          .estado("PENDIENTE")
          .build());
    }

    // 6. Crear orden
    Orden orden = Orden.builder()
        .codigoOrden(codigoOrden)
        .paciente(paciente)
        .medicoId(request.getMedicoId())
        .fechaRecepcion(LocalDateTime.now())
        .estado("PENDIENTE")
        .usuarioCreacionId(request.getUsuarioCreacionId())
        .total(total)
        .detalles(detalles)
        .build();

    // 7. Guardar
    Orden ordenGuardada = ordenRepository.save(orden);

    // 8. Retornar DTO
    return toDto(ordenGuardada);
  }

  private Paciente resolverPaciente(CreateOrdenRequest request) {
    if (request.getPacienteId() != null) {
      // Buscar paciente existente
      return pacienteRepository.findById(request.getPacienteId())
          .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));
    }

    // Crear nuevo paciente
    if (request.getNuevoPaciente() == null) {
      throw new IllegalArgumentException("Debe seleccionar un paciente existente o ingresar datos de uno nuevo");
    }

    CreateOrdenRequest.NuevoPacienteData datos = request.getNuevoPaciente();

    // Verificar si ya existe por documento
    var existente = pacienteRepository.findByDocumento(
        datos.getTipoDocumento(),
        datos.getNumeroDocumento());

    if (existente.isPresent()) {
      return existente.get();
    }

    // Crear nuevo
    Paciente nuevoPaciente = Paciente.builder()
        .tipoDocumento(datos.getTipoDocumento())
        .numeroDocumento(datos.getNumeroDocumento())
        .nombre(datos.getNombre())
        .fechaNacimiento(datos.getFechaNacimiento())
        .sexo(datos.getSexo())
        .direccion(datos.getDireccion())
        .telefono(datos.getTelefono())
        .email(datos.getEmail())
        .build();

    return pacienteRepository.save(nuevoPaciente);
  }

  private String generarCodigoOrden() {
    LocalDate hoy = LocalDate.now();
    String fechaParte = hoy.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    long contador = ordenRepository.countByFecha(hoy) + 1;
    return String.format("ORD-%s-%03d", fechaParte, contador);
  }

  private OrdenDto toDto(Orden orden) {
    List<OrdenDetalleDto> detallesDto = orden.getDetalles() != null
        ? orden.getDetalles().stream().map(this::toDetalleDto).collect(Collectors.toList())
        : List.of();

    return OrdenDto.builder()
        .id(orden.getId())
        .codigoOrden(orden.getCodigoOrden())
        .pacienteNombre(orden.getPaciente() != null ? orden.getPaciente().getNombre() : "")
        .pacienteDocumento(orden.getPaciente() != null
            ? orden.getPaciente().getTipoDocumento() + " " + orden.getPaciente().getNumeroDocumento()
            : "")
        .fechaRecepcion(orden.getFechaRecepcion())
        .estado(orden.getEstado())
        .total(orden.getTotal())
        .itemCount(detallesDto.size())
        .detalles(detallesDto)
        .build();
  }

  private OrdenDetalleDto toDetalleDto(OrdenDetalle detalle) {
    return OrdenDetalleDto.builder()
        .id(detalle.getId())
        .examenId(detalle.getExamen() != null ? detalle.getExamen().getId() : null)
        .examenCodigo(detalle.getExamen() != null ? detalle.getExamen().getCodigoInterno() : "")
        .examenNombre(detalle.getExamen() != null ? detalle.getExamen().getNombre() : "")
        .precioCobrado(detalle.getPrecioCobrado())
        .estado(detalle.getEstado())
        .build();
  }
}
