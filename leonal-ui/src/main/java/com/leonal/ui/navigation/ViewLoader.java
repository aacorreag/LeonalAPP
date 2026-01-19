package com.leonal.ui.navigation;

import javafx.scene.Parent;

// Abstraction for loading FXML views dynamically
public interface ViewLoader {
  Parent loadView(String fxmlPath);
}
