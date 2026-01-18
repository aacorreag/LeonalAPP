package com.leonal.launcher.config;

import com.leonal.application.usecase.paciente.CrearPacienteUseCase;
import com.leonal.application.usecase.paciente.ListarPacientesUseCase;
import com.leonal.application.usecase.auth.AutenticarUsuarioUseCase;
import com.leonal.domain.port.output.PacienteRepositoryPort;
import com.leonal.domain.port.output.UsuarioRepositoryPort;
import com.leonal.domain.port.output.PasswordEncoderPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

  @Bean
  public CrearPacienteUseCase crearPacienteUseCase(PacienteRepositoryPort pacienteRepository) {
    return new CrearPacienteUseCase(pacienteRepository);
  }

  @Bean
  public ListarPacientesUseCase listarPacientesUseCase(PacienteRepositoryPort pacienteRepository) {
    return new ListarPacientesUseCase(pacienteRepository);
  }

  @Bean
  public AutenticarUsuarioUseCase autenticarUsuarioUseCase(
      UsuarioRepositoryPort usuarioRepository,
      PasswordEncoderPort passwordEncoder) {
    return new AutenticarUsuarioUseCase(usuarioRepository, passwordEncoder);
  }
}
