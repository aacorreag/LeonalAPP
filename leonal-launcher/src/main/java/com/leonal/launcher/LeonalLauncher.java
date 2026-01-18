package com.leonal.launcher;

import com.leonal.ui.JavaFXMain;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "com.leonal")
public class LeonalLauncher {

  public static ConfigurableApplicationContext context;

  public static void main(String[] args) {
    // Start Spring Boot
    context = new SpringApplicationBuilder(LeonalLauncher.class)
        .headless(false) // Allow UI
        .run(args);

    // Configure JavaFX Wiring
    JavaFXMain.setControllerFactory(context::getBean);

    // Start JavaFX
    Application.launch(JavaFXMain.class, args);
  }
}
