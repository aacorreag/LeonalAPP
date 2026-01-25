package com.leonal.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXMain extends Application {

  private static javafx.util.Callback<Class<?>, Object> controllerFactory;

  public static void setControllerFactory(javafx.util.Callback<Class<?>, Object> factory) {
    controllerFactory = factory;
  }

  @Override
  public void start(Stage stage) {
    try {
      // Load Login instead of Pacientes initially
      javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/login.fxml"));
      if (controllerFactory != null) {
        loader.setControllerFactory(controllerFactory);
      }

      javafx.scene.Parent root = loader.load();
      Scene scene = new Scene(root, 900, 600); // Standard minimal size
      scene.getStylesheets().add(
              java.util.Objects.requireNonNull(
                      getClass().getResource("/css/styles.css"),
                      "No se encontró css/styles.css en el classpath"
              ).toExternalForm()
      );

      stage.setScene(scene);
      stage.setTitle("leonalApp - Login");
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("CRITICAL UI ERROR: " + e.getMessage());
    }
  }
}
