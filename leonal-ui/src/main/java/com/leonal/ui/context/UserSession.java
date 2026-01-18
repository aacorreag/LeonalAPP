package com.leonal.ui.context;

import com.leonal.application.dto.auth.AuthenticatedUser;

// Mutable state class to hold current session in UI layer
public class UserSession {
  private AuthenticatedUser currentUser;

  public void setCurrentUser(AuthenticatedUser user) {
    this.currentUser = user;
  }

  public AuthenticatedUser getCurrentUser() {
    return currentUser;
  }

  public boolean isLoggedIn() {
    return currentUser != null;
  }

  public void logout() {
    this.currentUser = null;
  }
}
