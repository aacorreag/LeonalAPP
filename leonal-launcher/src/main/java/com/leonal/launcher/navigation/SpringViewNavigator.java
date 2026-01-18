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
  public void navigateToDashboard() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pacientes.fxml"));
      loader.setControllerFactory(springContext::getBean);
      Parent root = loader.load();

      Stage dashboardStage = new Stage();
      dashboardStage.setTitle("leonalApp - Dashboard");
      dashboardStage.setScene(new Scene(root, 900, 600));
      dashboardStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void closeLogin() {
    // We find the active window by a hack or passed reference.
    // For simplicity in this vertical slice, we rely on the Controller to close
    // itself
    // OR we track the stage. Ideally, navigator should manage stages.
    // Here, we just assume the old windows are closed by the user or hidden.
    Window.getWindows().stream()
        .filter(Window::isShowing)
        .findFirst()
        .ifPresent(Window::hide);
  }
}
