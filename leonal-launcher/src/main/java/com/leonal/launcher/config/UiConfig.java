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
import com.leonal.application.usecase.report.GenerarComprobanteOrdenUseCase;
import com.leonal.ui.controller.PacienteController;
import com.leonal.ui.controller.LoginController;
import com.leonal.ui.controller.MainLayoutController;
import com.leonal.ui.controller.DashboardController;
import com.leonal.ui.controller.UsuariosController;
import com.leonal.ui.controller.ExamenesController;
import com.leonal.ui.controller.OrdenesController;
import com.leonal.ui.controller.NuevaOrdenController;
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
import org.springframework.context.annotation.Scope;

@Configuration
public class UiConfig {

  @Bean
  public UserSession userSession() {
    return new UserSession();
  }

  @Bean
  public ViewNavigator viewNavigator(ApplicationContext context) {
    return new SpringViewNavigator(context);
  }

  @Bean
  public ViewLoader viewLoader(ApplicationContext context) {
    return new SpringViewLoader(context);
  }

  // Controllers

  @Bean
  public PacienteController pacienteController(
      CrearPacienteUseCase crearPacienteUseCase,
      ListarPacientesUseCase listarPacientesUseCase) {
    return new PacienteController(crearPacienteUseCase, listarPacientesUseCase);
  }

  @Bean
  public LoginController loginController(
      AutenticarUsuarioUseCase authUseCase,
      UserSession userSession,
      ViewNavigator viewNavigator) {
    return new LoginController(authUseCase, userSession, viewNavigator);
  }

  @Bean
  public MainLayoutController mainLayoutController(
      UserSession userSession,
      ViewLoader viewLoader,
      ViewNavigator viewNavigator) {
    return new MainLayoutController(userSession, viewLoader, viewNavigator);
  }

  @Bean
  public DashboardController dashboardController(UserSession userSession, ApplicationEventPublisher eventPublisher) {
    return new DashboardController(userSession, eventPublisher);
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
  @Scope("prototype")
  public OrdenesController ordenesController(
      ListarOrdenesUseCase listarOrdenes,
      GenerarComprobanteOrdenUseCase generarComprobanteOrden,
      ApplicationContext applicationContext) {
    return new OrdenesController(listarOrdenes, generarComprobanteOrden, applicationContext);
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
}
