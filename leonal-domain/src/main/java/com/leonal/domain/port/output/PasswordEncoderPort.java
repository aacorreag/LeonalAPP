package com.leonal.domain.port.output;

import com.leonal.domain.model.Usuario;
import java.util.Optional;

public interface PasswordEncoderPort {
  String encode(String rawPassword);

  boolean matches(String rawPassword, String encodedPassword);
}
