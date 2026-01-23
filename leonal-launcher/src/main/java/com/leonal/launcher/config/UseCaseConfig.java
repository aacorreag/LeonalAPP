package com.leonal.launcher.config;

import com.leonal.application.usecase.paciente.CrearPacienteUseCase;
import com.leonal.application.usecase.paciente.ListarPacientesUseCase;
import com.leonal.application.usecase.auth.AutenticarUsuarioUseCase;
import com.leonal.application.usecase.user.GuardarUsuarioUseCase;
import com.leonal.application.usecase.examen.ListarExamenesUseCase;
import com.leonal.application.usecase.examen.GuardarExamenUseCase;
import com.leonal.application.usecase.user.ListarRolesUseCase;
import com.leonal.application.usecase.user.ListarUsuariosUseCase;
import com.leonal.application.usecase.orden.CrearOrdenUseCase;
import com.leonal.application.usecase.orden.ListarOrdenesUseCase;
import com.leonal.application.usecase.orden.ActualizarEstadoOrdenUseCase;
import com.leonal.application.usecase.orden.ObtenerOrdenPorIdUseCase;
import com.leonal.application.usecase.report.GenerarComprobanteOrdenUseCase;
import com.leonal.application.usecase.resultado.IngresarResultadosUseCase;
import com.leonal.application.usecase.resultado.ValidarOrdenUseCase;
import com.leonal.application.usecase.resultado.GenerarReporteResultadosUseCase;
import com.leonal.application.usecase.factura.CrearFacturaUseCase;
import com.leonal.application.usecase.factura.ListarFacturasUseCase;
import com.leonal.application.usecase.factura.ActualizarEstadoFacturaUseCase;
import com.leonal.application.usecase.pago.RegistrarPagoUseCase;
import com.leonal.application.usecase.pago.ListarPagosUseCase;
import com.leonal.application.usecase.caja.AbrirCajaUseCase;
import com.leonal.application.usecase.caja.CerrarCajaUseCase;
import com.leonal.application.usecase.caja.ListarCajasUseCase;
import com.leonal.application.usecase.caja.ActualizarTotalCajaUseCase;
import com.leonal.domain.port.output.PacienteRepositoryPort;
import com.leonal.domain.port.output.UsuarioRepositoryPort;
import com.leonal.domain.port.output.ReportRepositoryPort;
import com.leonal.domain.port.output.RolRepositoryPort;
import com.leonal.domain.port.output.ExamenRepositoryPort;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import com.leonal.domain.port.output.ResultadoRepositoryPort;
import com.leonal.domain.port.output.PasswordEncoderPort;
import com.leonal.domain.port.output.FacturaRepositoryPort;
import com.leonal.domain.port.output.PagoRepositoryPort;
import com.leonal.domain.port.output.CajaRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  @Bean
  public CrearPacienteUseCase crearPacienteUseCase(PacienteRepositoryPort repository) {
    return new CrearPacienteUseCase(repository);
  }

  @Bean
  public ListarPacientesUseCase listarPacientesUseCase(PacienteRepositoryPort repository) {
    return new ListarPacientesUseCase(repository);
  }

  @Bean
  public AutenticarUsuarioUseCase autenticarUsuarioUseCase(UsuarioRepositoryPort repository,
      PasswordEncoderPort encoder) {
    return new AutenticarUsuarioUseCase(repository, encoder);
  }

  // User Management Use Cases

  @Bean
  public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioRepositoryPort repository) {
    return new ListarUsuariosUseCase(repository);
  }

  @Bean
  public ListarRolesUseCase listarRolesUseCase(RolRepositoryPort repository) {
    return new ListarRolesUseCase(repository);
  }

  @Bean
  public GuardarUsuarioUseCase guardarUsuarioUseCase(UsuarioRepositoryPort userRepo, RolRepositoryPort rolRepo,
      PasswordEncoderPort encoder) {
    return new GuardarUsuarioUseCase(userRepo, rolRepo, encoder);
  }

  @Bean
  public ListarExamenesUseCase listarExamenesUseCase(ExamenRepositoryPort repository) {
    return new ListarExamenesUseCase(repository);
  }

  @Bean
  public GuardarExamenUseCase guardarExamenUseCase(ExamenRepositoryPort repository) {
    return new GuardarExamenUseCase(repository);
  }

  // Orden Use Cases

  @Bean
  public CrearOrdenUseCase crearOrdenUseCase(
      OrdenRepositoryPort ordenRepository,
      PacienteRepositoryPort pacienteRepository,
      ExamenRepositoryPort examenRepository) {
    return new CrearOrdenUseCase(ordenRepository, pacienteRepository, examenRepository);
  }

  @Bean
  public ListarOrdenesUseCase listarOrdenesUseCase(OrdenRepositoryPort ordenRepository) {
    return new ListarOrdenesUseCase(ordenRepository);
  }

  @Bean
  public ActualizarEstadoOrdenUseCase actualizarEstadoOrdenUseCase(OrdenRepositoryPort ordenRepository) {
    return new ActualizarEstadoOrdenUseCase(ordenRepository);
  }

  @Bean
  public ObtenerOrdenPorIdUseCase obtenerOrdenPorIdUseCase(OrdenRepositoryPort ordenRepository) {
    return new ObtenerOrdenPorIdUseCase(ordenRepository);
  }

  @Bean
  public GenerarComprobanteOrdenUseCase generarComprobanteOrdenUseCase(
      OrdenRepositoryPort ordenRepository,
      ReportRepositoryPort reportRepository) {
    return new GenerarComprobanteOrdenUseCase(ordenRepository, reportRepository);
  }

  // Resultado Use Cases

  @Bean
  public IngresarResultadosUseCase ingresarResultadosUseCase(
      ResultadoRepositoryPort resultadoRepository,
      OrdenRepositoryPort ordenRepository) {
    return new IngresarResultadosUseCase(resultadoRepository, ordenRepository);
  }

  @Bean
  public ValidarOrdenUseCase validarOrdenUseCase(OrdenRepositoryPort ordenRepository) {
    return new ValidarOrdenUseCase(ordenRepository);
  }

  @Bean
  public GenerarReporteResultadosUseCase generarReporteResultadosUseCase(
      OrdenRepositoryPort ordenRepository,
      ReportRepositoryPort reportRepository) {
    return new GenerarReporteResultadosUseCase(reportRepository, ordenRepository);
  }

  // Facturación Use Cases

  @Bean
  public CrearFacturaUseCase crearFacturaUseCase(FacturaRepositoryPort facturaRepository) {
    return new CrearFacturaUseCase(facturaRepository);
  }

  @Bean
  public ListarFacturasUseCase listarFacturasUseCase(FacturaRepositoryPort facturaRepository) {
    return new ListarFacturasUseCase(facturaRepository);
  }

  @Bean
  public ActualizarEstadoFacturaUseCase actualizarEstadoFacturaUseCase(FacturaRepositoryPort facturaRepository) {
    return new ActualizarEstadoFacturaUseCase(facturaRepository);
  }

  @Bean
  public RegistrarPagoUseCase registrarPagoUseCase(
      PagoRepositoryPort pagoRepository,
      FacturaRepositoryPort facturaRepository) {
    return new RegistrarPagoUseCase(pagoRepository, facturaRepository);
  }

  @Bean
  public ListarPagosUseCase listarPagosUseCase(PagoRepositoryPort pagoRepository) {
    return new ListarPagosUseCase(pagoRepository);
  }

  @Bean
  public AbrirCajaUseCase abrirCajaUseCase(CajaRepositoryPort cajaRepository) {
    return new AbrirCajaUseCase(cajaRepository);
  }

  @Bean
  public CerrarCajaUseCase cerrarCajaUseCase(CajaRepositoryPort cajaRepository) {
    return new CerrarCajaUseCase(cajaRepository);
  }

  @Bean
  public ListarCajasUseCase listarCajasUseCase(CajaRepositoryPort cajaRepository) {
    return new ListarCajasUseCase(cajaRepository);
  }

  @Bean
  public ActualizarTotalCajaUseCase actualizarTotalCajaUseCase(CajaRepositoryPort cajaRepository) {
    return new ActualizarTotalCajaUseCase(cajaRepository);
  }
}
