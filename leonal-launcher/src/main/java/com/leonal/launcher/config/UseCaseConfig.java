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
import com.leonal.application.usecase.report.GenerarComprobanteOrdenUseCase;
import com.leonal.domain.port.output.PacienteRepositoryPort;
import com.leonal.domain.port.output.UsuarioRepositoryPort;
import com.leonal.domain.port.output.ReportRepositoryPort;
import com.leonal.domain.port.output.RolRepositoryPort;
import com.leonal.domain.port.output.ExamenRepositoryPort;
import com.leonal.domain.port.output.OrdenRepositoryPort;
import com.leonal.domain.port.output.PasswordEncoderPort;
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
  public GenerarComprobanteOrdenUseCase generarComprobanteOrdenUseCase(
      OrdenRepositoryPort ordenRepository,
      ReportRepositoryPort reportRepository) {
    return new GenerarComprobanteOrdenUseCase(ordenRepository, reportRepository);
  }
}
