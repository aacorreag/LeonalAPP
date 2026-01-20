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
  private Button btnInicio, btnPacientes, btnOrdenes, btnResultados, btnCatalogo, btnUsuarios;
  @FXML
  private Label lblAdminSection; // Label header for admin

  public void initialize() {
    setupUserHeader();
    setupMenu();
    goHome(); // Updated to use the method with highlight
  }

  @EventListener
  public void onNavigationEvent(NavigationEvent event) {
    loadCenterView(event.getFxmlPath());
    updateActiveLinkByPath(event.getFxmlPath());
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

  private void loadCenterView(String fxmlPath) {
    Parent view = viewLoader.loadView(fxmlPath);
    if (view != null) {
      mainContainer.setCenter(view);
    }
  }

  private void updateActiveLink(Button activeBtn) {
    Button[] allButtons = { btnInicio, btnPacientes, btnOrdenes, btnResultados, btnCatalogo, btnUsuarios };
    for (Button btn : allButtons) {
      if (btn != null) {
        btn.getStyleClass().remove("menu-button-active");
      }
    }
    if (activeBtn != null) {
      activeBtn.getStyleClass().add("menu-button-active");
    }
  }

  private void updateActiveLinkByPath(String fxmlPath) {
    if (fxmlPath.contains("dashboard"))
      updateActiveLink(btnInicio);
    else if (fxmlPath.contains("pacientes"))
      updateActiveLink(btnPacientes);
    else if (fxmlPath.contains("ordenes"))
      updateActiveLink(btnOrdenes);
    else if (fxmlPath.contains("resultados"))
      updateActiveLink(btnResultados);
    else if (fxmlPath.contains("examenes"))
      updateActiveLink(btnCatalogo);
    else if (fxmlPath.contains("usuarios"))
      updateActiveLink(btnUsuarios);
  }

  // --- Menu Actions ---

  @FXML
  public void goHome() {
    loadCenterView("/fxml/dashboard.fxml");
    updateActiveLink(btnInicio);
  }

  @FXML
  public void goPacientes() {
    loadCenterView("/fxml/pacientes.fxml");
    updateActiveLink(btnPacientes);
  }

  @FXML
  public void goExamenes() {
    loadCenterView("/fxml/examenes.fxml");
    updateActiveLink(btnCatalogo);
  }

  @FXML
  public void goOrdenes() {
    loadCenterView("/fxml/ordenes.fxml");
    updateActiveLink(btnOrdenes);
  }

  @FXML
  public void goResultados() {
    loadCenterView("/fxml/resultados.fxml");
    updateActiveLink(btnResultados);
  }

  @FXML
  public void goUsuarios() {
    loadCenterView("/fxml/usuarios.fxml");
    updateActiveLink(btnUsuarios);
  }

  @FXML
  public void logout() {
    session.logout();
    navigator.closeCurrentWindow();
    navigator.navigateToLogin();
  }
}
