package com.leonal.ui.controller;

import com.leonal.ui.context.UserSession;
import com.leonal.ui.event.NavigationEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class DashboardController {

  private final UserSession session;
  private final ApplicationEventPublisher eventPublisher;
  private final com.leonal.application.usecase.dashboard.ObtenerDashboardStatsUseCase obtenerStats;

  @FXML
  private Label lblWelcome;

  // KPI Labels
  @FXML
  private Label lblIngresosHoy;
  @FXML
  private Label lblOrdenesHoy;
  @FXML
  private Label lblPendientes;

  // Chart
  @FXML
  private javafx.scene.chart.BarChart<String, Number> barChartSemana;

  @FXML
  public void initialize() {
    if (session.isLoggedIn()) {
      lblWelcome.setText("Bienvenido, " + session.getCurrentUser().getNombreCompleto());
    }

    cargarEstadisticas();
  }

  private void cargarEstadisticas() {
    if (obtenerStats == null)
      return; // Prevención en caso de problemas de DI

    // Ejecutar en background simplificado para no bloquear UI principal
    javafx.concurrent.Task<com.leonal.application.dto.dashboard.DashboardStatsDto> task = new javafx.concurrent.Task<>() {
      @Override
      protected com.leonal.application.dto.dashboard.DashboardStatsDto call() throws Exception {
        return obtenerStats.execute();
      }
    };

    task.setOnSucceeded(e -> {
      var stats = task.getValue();
      actualizarUI(stats);
    });

    task.setOnFailed(e -> {
      Throwable ex = task.getException();
      ex.printStackTrace();
      javafx.application.Platform.runLater(() -> {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error de Datos");
        alert.setHeaderText("No se pudieron cargar las estadísticas");
        alert.setContentText(ex.getMessage());
        alert.showAndWait();
      });
    });

    new Thread(task).start();
  }

  private void actualizarUI(com.leonal.application.dto.dashboard.DashboardStatsDto stats) {
    if (stats == null)
      return;

    lblIngresosHoy.setText(String.format("$ %,.0f", stats.getIngresosHoy()));
    lblOrdenesHoy.setText(String.valueOf(stats.getOrdenesHoy()));
    lblPendientes.setText(String.valueOf(stats.getResultadosPendientes()));

    // Chart Update
    barChartSemana.getData().clear();
    javafx.scene.chart.XYChart.Series<String, Number> series = new javafx.scene.chart.XYChart.Series<>();
    series.setName("Órdenes");

    if (stats.getOrdenesSemana() != null) {
      stats.getOrdenesSemana().forEach((dia, cantidad) -> {
        series.getData().add(new javafx.scene.chart.XYChart.Data<>(dia, cantidad));
      });
    }

    barChartSemana.getData().add(series);
  }

  @FXML
  public void goPacientes() {
    eventPublisher.publishEvent(new NavigationEvent(this, "/fxml/pacientes.fxml"));
  }

  // Placeholder for WIP
  @FXML
  public void goOrdenes() {
    eventPublisher.publishEvent(new NavigationEvent(this, "/fxml/ordenes.fxml"));
  }

  @FXML
  public void goExamenes() {
    eventPublisher.publishEvent(new NavigationEvent(this, "/fxml/examenes.fxml"));
  }
}
