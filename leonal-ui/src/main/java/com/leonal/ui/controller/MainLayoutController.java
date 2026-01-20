package com.leonal.ui.controller;

import com.leonal.application.dto.auth.AuthenticatedUser;
import com.leonal.ui.context.UserSession;
import com.leonal.ui.event.NavigationEvent;
import com.leonal.ui.navigation.ViewLoader;
import com.leonal.ui.navigation.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class MainLayoutController {

  private final UserSession session;
  private final ViewLoader viewLoader;
  private final ViewNavigator navigator;

  @FXML
  private BorderPane mainContainer;
  @FXML
  private Label lblUserStatus;
  @FXML
  private Label lblRoleStatus; // Added field
  @FXML
  private VBox sideMenu;
  @FXML
  private Button btnUsuarios; // Admin only
  @FXML
  private Label lblAdminSection; // Label header for admin

  public void initialize() {
    setupUserHeader();
    setupMenu();
    loadHome();
  }

  @EventListener
  public void onNavigationEvent(NavigationEvent event) {
    loadCenterView(event.getFxmlPath());
  }

  private void setupUserHeader() {
    if (session.isLoggedIn()) {
      AuthenticatedUser u = session.getCurrentUser();
      lblUserStatus.setText(u.getUsername()); // Just username
      lblRoleStatus.setText(u.getRoles().toString()); // Roles below
    }
  }

  private void setupMenu() {
    if (session.isLoggedIn()) {
      boolean isAdmin = session.getCurrentUser().getRoles().contains("ADMIN");
      if (!isAdmin) {
        if (btnUsuarios != null) {
          btnUsuarios.setVisible(false);
          btnUsuarios.setManaged(false);
        }
        if (lblAdminSection != null) {
          lblAdminSection.setVisible(false);
          lblAdminSection.setManaged(false);
        }
      }
    }
  }

  private void loadHome() {
    loadCenterView("/fxml/dashboard.fxml");
  }

  private void loadCenterView(String fxmlPath) {
    Parent view = viewLoader.loadView(fxmlPath);
    if (view != null) {
      mainContainer.setCenter(view);
    }
  }

  // --- Menu Actions ---

  @FXML
  public void goHome() {
    loadCenterView("/fxml/dashboard.fxml");
  }

  @FXML
  public void goPacientes() {
    loadCenterView("/fxml/pacientes.fxml");
  }

  @FXML
  public void goExamenes() {
    loadCenterView("/fxml/examenes.fxml");
  }

  @FXML
  public void goOrdenes() {
    loadCenterView("/fxml/ordenes.fxml");
  }

  @FXML
  public void goResultados() {
    loadCenterView("/fxml/resultados.fxml");
  }

  @FXML
  public void goUsuarios() {
    loadCenterView("/fxml/usuarios.fxml");
  }

  @FXML
  public void logout() {
    session.logout();
    navigator.closeCurrentWindow();
    navigator.navigateToLogin();
  }
}
