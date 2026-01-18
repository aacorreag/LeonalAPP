package com.leonal.launcher;

import com.leonal.ui.JavaFXMain;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "com.leonal")
@EnableJpaRepositories(basePackages = "com.leonal.infrastructure.persistence.repository")
@EntityScan(basePackages = "com.leonal.infrastructure.persistence.entity")
public class LeonalLauncher {

  public static ConfigurableApplicationContext context;

  public static void main(String[] args) {
    context = new SpringApplicationBuilder(LeonalLauncher.class)
        .headless(false)
        .run(args);

    JavaFXMain.setControllerFactory(context::getBean);
    Application.launch(JavaFXMain.class, args);
  }
}
