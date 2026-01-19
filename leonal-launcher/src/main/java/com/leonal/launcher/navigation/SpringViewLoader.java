package com.leonal.launcher.navigation;

import com.leonal.ui.navigation.ViewLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

@RequiredArgsConstructor
public class SpringViewLoader implements ViewLoader {

  private final ApplicationContext springContext;

  @Override
  public Parent loadView(String fxmlPath) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      loader.setControllerFactory(springContext::getBean);
      return loader.load();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
