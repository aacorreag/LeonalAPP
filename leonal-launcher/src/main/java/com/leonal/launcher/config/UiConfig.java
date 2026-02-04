package com.leonal.launcher.config;

import com.leonal.application.usecase.paciente.CrearPacienteUseCase;
import com.leonal.application.usecase.paciente.ListarPacientesUseCase;
import com.leonal.application.usecase.auth.AutenticarUsuarioUseCase;
import com.leonal.application.usecase.user.GuardarUsuarioUseCase;
import com.leonal.application.usecase.user.ListarRolesUseCase;
import com.leonal.application.usecase.user.ListarUsuariosUseCase;
import com.leonal.application.usecase.examen.ListarExamenesUseCase;
import com.leonal.application.usecase.examen.GuardarExamenUseCase;
import com.leonal.application.usecase.orden.CrearOrdenUseCase;
import com.leonal.application.usecase.orden.ListarOrdenesUseCase;
import com.leonal.application.usecase.orden.ObtenerOrdenPorIdUseCase;
import com.leonal.application.usecase.orden.ListarOrdenesSinFacturaUseCase;
import com.leonal.application.usecase.report.GenerarComprobanteOrdenUseCase;
import com.leonal.application.usecase.resultado.IngresarResultadosUseCase;
import com.leonal.application.usecase.resultado.ValidarOrdenUseCase;
import com.leonal.application.usecase.resultado.GenerarReporteResultadosUseCase;
import com.leonal.application.usecase.factura.CrearFacturaUseCase;
import com.leonal.application.usecase.factura.ListarFacturasUseCase;
import com.leonal.application.usecase.pago.RegistrarPagoUseCase;
import com.leonal.application.usecase.pago.ListarPagosUseCase;
import com.leonal.application.usecase.caja.AbrirCajaUseCase;
import com.leonal.application.usecase.caja.CerrarCajaUseCase;
import com.leonal.application.usecase.caja.ListarCajasUseCase;
import com.leonal.application.usecase.caja.ActualizarTotalCajaUseCase;
import com.leonal.ui.controller.*;
import com.leonal.ui.context.UserSession;
import com.leonal.ui.navigation.ViewNavigator;
import com.leonal.ui.navigation.ViewLoader;
import com.leonal.launcher.navigation.SpringViewNavigator;
import com.leonal.launcher.navigation.SpringViewLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class UiConfig {

  @Bean
  public UserSession userSession() {
    return new UserSession();
  }

  @Bean
  public ViewLoader viewLoader(ApplicationContext applicationContext) {
    return new SpringViewLoader(applicationContext);
  }

  @Bean
  public ViewNavigator viewNavigator(ApplicationContext applicationContext) {
    return new SpringViewNavigator(applicationContext);
  }

  @Bean
  public LoginController loginController(AutenticarUsuarioUseCase autenticarUsuario, UserSession userSession,
      ViewNavigator viewNavigator) {
    return new LoginController(autenticarUsuario, userSession, viewNavigator);
  }

  @Bean
  public MainLayoutController mainLayoutController(UserSession userSession, ViewLoader viewLoader,
      ViewNavigator viewNavigator) {
    return new MainLayoutController(userSession, viewLoader, viewNavigator);
  }

  @Bean
  public com.leonal.application.usecase.dashboard.ObtenerDashboardStatsUseCase obtenerDashboardStatsUseCase(
      com.leonal.domain.port.output.OrdenRepositoryPort ordenRepository,
      com.leonal.domain.port.output.FacturaRepositoryPort facturaRepository) {
    return new com.leonal.application.usecase.dashboard.ObtenerDashboardStatsUseCase(ordenRepository,
        facturaRepository);
  }

  @Bean
  public DashboardController dashboardController(UserSession userSession, ApplicationEventPublisher eventPublisher,
      com.leonal.application.usecase.dashboard.ObtenerDashboardStatsUseCase obtenerStats) {
    return new DashboardController(userSession, eventPublisher, obtenerStats);
  }

  @Bean
  public PacienteController pacienteController(
      CrearPacienteUseCase crearPaciente,
      ListarPacientesUseCase listarPacientes) {
    return new PacienteController(crearPaciente, listarPacientes);
  }

  @Bean
  public UsuariosController usuariosController(
      ListarUsuariosUseCase listarUsuarios,
      GuardarUsuarioUseCase guardarUsuario,
      ListarRolesUseCase listarRoles) {
    return new UsuariosController(listarUsuarios, guardarUsuario, listarRoles);
  }

  @Bean
  public ExamenesController examenesController(
      ListarExamenesUseCase listarExamenes,
      GuardarExamenUseCase guardarExamen,
      UserSession userSession) {
    return new ExamenesController(listarExamenes, guardarExamen, userSession);
  }

  @Bean
  public OrdenesController ordenesController(
      ListarOrdenesUseCase listarOrdenes,
      GenerarComprobanteOrdenUseCase generarComprobante,
      ApplicationContext applicationContext) {
    return new OrdenesController(listarOrdenes, generarComprobante, applicationContext);
  }

  @Bean
  @Scope("prototype")
  public NuevaOrdenController nuevaOrdenController(
      CrearOrdenUseCase crearOrden,
      ListarExamenesUseCase listarExamenes,
      ListarPacientesUseCase listarPacientes,
      UserSession userSession) {
    return new NuevaOrdenController(crearOrden, listarExamenes, listarPacientes, userSession);
  }

  @Bean
  public ResultadosController resultadosController(
      ListarOrdenesUseCase listarOrdenes,
      IngresarResultadosUseCase ingresarResultados,
      ValidarOrdenUseCase validarOrden,
      GenerarReporteResultadosUseCase generarReporte,
      UserSession userSession,
      ApplicationContext applicationContext) {
    return new ResultadosController(listarOrdenes, ingresarResultados, validarOrden, generarReporte, userSession,
        applicationContext);
  }

  @Bean
  @Scope("prototype")
  public IngresoResultadosDialogController ingresoResultadosDialogController(
      IngresarResultadosUseCase ingresarResultados,
      UserSession userSession) {
    return new IngresoResultadosDialogController(ingresarResultados, userSession);
  }

  // Facturación Controllers

  @Bean
  public com.leonal.application.usecase.factura.GenerarReporteFacturaUseCase generarReporteFacturaUseCase(
      com.leonal.domain.port.output.FacturaRepositoryPort facturaRepository,
      com.leonal.domain.port.output.OrdenRepositoryPort ordenRepository,
      com.leonal.domain.port.output.ReportRepositoryPort reportRepository) {
    return new com.leonal.application.usecase.factura.GenerarReporteFacturaUseCase(facturaRepository, ordenRepository,
        reportRepository);
  }

  @Bean
  public FacturacionController facturacionController(
      CrearFacturaUseCase crearFactura,
      ListarFacturasUseCase listarFacturas,
      ListarOrdenesUseCase listarOrdenes,
      ListarOrdenesSinFacturaUseCase listarOrdenesSinFactura,
      ObtenerOrdenPorIdUseCase obtenerOrden,
      ActualizarTotalCajaUseCase actualizarTotalCaja,
      ListarCajasUseCase listarCajas,
      com.leonal.application.usecase.factura.GenerarReporteFacturaUseCase generarReporteFactura,
      UserSession userSession) {
    return new FacturacionController(crearFactura, listarFacturas, listarOrdenes, listarOrdenesSinFactura, obtenerOrden,
        actualizarTotalCaja, listarCajas, generarReporteFactura, userSession);
  }

  @Bean
  public PagoController pagoController(
      RegistrarPagoUseCase registrarPago,
      ListarPagosUseCase listarPagos,
      ListarFacturasUseCase listarFacturas,
      ListarCajasUseCase listarCajas,
      ActualizarTotalCajaUseCase actualizarTotalCaja,
      UserSession userSession) {
    return new PagoController(registrarPago, listarPagos, listarFacturas, listarCajas, actualizarTotalCaja,
        userSession);
  }

  @Bean
  public CajaController cajaController(
      AbrirCajaUseCase abrirCaja,
      CerrarCajaUseCase cerrarCaja,
      ListarCajasUseCase listarCajas,
      UserSession userSession) {
    return new CajaController(abrirCaja, cerrarCaja, listarCajas, userSession);
  }
}
