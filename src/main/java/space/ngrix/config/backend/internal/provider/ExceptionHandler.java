package space.ngrix.config.backend.internal.provider;

import space.ngrix.config.backend.internal.exceptions.SimplixValidationException;
import lombok.NonNull;

public abstract class ExceptionHandler {

  public RuntimeException create(
      @NonNull final Throwable throwable,
      @NonNull final String... messages) {
    return new SimplixValidationException(
        throwable,
        messages);
  }
}
