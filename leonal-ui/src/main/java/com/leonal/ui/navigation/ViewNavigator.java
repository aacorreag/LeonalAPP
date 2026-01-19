package com.leonal.ui.navigation;

public interface ViewNavigator {
  void navigateToMainLayout(); // Replaces navigateToDashboard

  void navigateToLogin();

  void closeCurrentWindow();
}
