package com.leonal.launcher.config;

import com.leonal.application.usecase.paciente.CrearPacienteUseCase;
import com.leonal.application.usecase.paciente.ListarPacientesUseCase;
import com.leonal.ui.controller.PacienteController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UiConfig {

  @Bean
  public PacienteController pacienteController(
      CrearPacienteUseCase crearPacienteUseCase,
      ListarPacientesUseCase listarPacientesUseCase) {
    return new PacienteController(crearPacienteUseCase, listarPacientesUseCase);
  }
}
