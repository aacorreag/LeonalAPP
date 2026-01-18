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
      javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/pacientes.fxml"));
      if (controllerFactory != null) {
        loader.setControllerFactory(controllerFactory);
      }

      javafx.scene.Parent root = loader.load();
      Scene scene = new Scene(root, 900, 600);

      stage.setScene(scene);
      stage.setTitle("leonalApp - Clinical System");
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
      // Show error in a rudimentary way if UI fails
      System.err.println("CRITICAL UI ERROR: " + e.getMessage());
    }
  }
}
