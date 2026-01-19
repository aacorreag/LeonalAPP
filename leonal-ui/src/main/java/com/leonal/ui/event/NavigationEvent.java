package com.leonal.ui.event;

import org.springframework.context.ApplicationEvent;

public class NavigationEvent extends ApplicationEvent {
  private final String fxmlPath;

  public NavigationEvent(Object source, String fxmlPath) {
    super(source);
    this.fxmlPath = fxmlPath;
  }

  public String getFxmlPath() {
    return fxmlPath;
  }
}
