package com.leonal.ui.controller;

import com.leonal.application.dto.auth.AuthenticatedUser;
import com.leonal.application.dto.auth.LoginRequest;
import com.leonal.application.usecase.auth.AutenticarUsuarioUseCase;
import com.leonal.ui.context.UserSession;
import com.leonal.ui.navigation.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginController {

  private final AutenticarUsuarioUseCase authUseCase;
  private final UserSession userSession;
  private final ViewNavigator viewNavigator;

  @FXML
  private TextField txtUsername;
  @FXML
  private PasswordField txtPassword;

  @FXML
  public void login() {
    try {
      LoginRequest request = LoginRequest.builder()
          .username(txtUsername.getText())
          .password(txtPassword.getText())
          .build();

      AuthenticatedUser user = authUseCase.execute(request);
      userSession.setCurrentUser(user);

      mostrarAlerta("Bienvenido", "Acceso concedido a: " + user.getNombreCompleto());

      // Navigate using interface
      viewNavigator.closeLogin();
      viewNavigator.navigateToDashboard();

    } catch (IllegalArgumentException e) {
      mostrarAlerta("Error de Acceso", "Credenciales Inválidas");
    } catch (Exception e) {
      e.printStackTrace();
      mostrarAlerta("Error Crítico", "Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  private void mostrarAlerta(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
  }

  // Helper to close self if needed by controller logic, but Navigator handles
  // main flow
  public void closeWindow() {
    if (txtUsername.getScene() != null && txtUsername.getScene().getWindow() != null) {
      txtUsername.getScene().getWindow().hide();
    }
  }
}
