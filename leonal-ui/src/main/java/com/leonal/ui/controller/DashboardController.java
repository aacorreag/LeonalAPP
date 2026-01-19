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

  @FXML
  private Label lblWelcome;

  public void initialize() {
    if (session.isLoggedIn()) {
      lblWelcome.setText("Bienvenido, " + session.getCurrentUser().getNombreCompleto());
    }
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
