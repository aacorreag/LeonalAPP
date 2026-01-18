package com.leonal.launcher.config;

import com.leonal.application.usecase.paciente.CrearPacienteUseCase;
import com.leonal.application.usecase.paciente.ListarPacientesUseCase;
import com.leonal.application.usecase.auth.AutenticarUsuarioUseCase;
import com.leonal.ui.controller.PacienteController;
import com.leonal.ui.controller.LoginController;
import com.leonal.ui.context.UserSession;
import com.leonal.ui.navigation.ViewNavigator;
import com.leonal.launcher.navigation.SpringViewNavigator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UiConfig {

  @Bean
  public UserSession userSession() {
    return new UserSession(); // UI Scope / Singleton for Desktop
  }

  @Bean
  public ViewNavigator viewNavigator(ApplicationContext context) {
    return new SpringViewNavigator(context);
  }

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
}
