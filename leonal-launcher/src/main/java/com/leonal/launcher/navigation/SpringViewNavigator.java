package com.leonal.launcher.navigation;

import com.leonal.ui.navigation.ViewNavigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

@RequiredArgsConstructor
public class SpringViewNavigator implements ViewNavigator {

  private final ApplicationContext springContext;

  @Override
  public void navigateToMainLayout() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_layout.fxml"));
      loader.setControllerFactory(springContext::getBean);
      Parent root = loader.load();

      Stage mainStage = new Stage();
      mainStage.setTitle("leonalApp - Clinical System");
      mainStage.setScene(new Scene(root, 1000, 700));
      mainStage.setMaximized(true);
      mainStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void navigateToLogin() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
      loader.setControllerFactory(springContext::getBean);
      Parent root = loader.load();

      Stage loginStage = new Stage();
      loginStage.setTitle("leonalApp - Login");
      loginStage.setScene(new Scene(root, 900, 600));
      loginStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void closeCurrentWindow() {
    // Simple strategy: Close active window.
    // In complex apps, we might pass the Stage source.
    Window.getWindows().stream()
        .filter(Window::isShowing)
        .filter(w -> w instanceof Stage)
        .findFirst()
        .ifPresent(Window::hide);
  }
}
