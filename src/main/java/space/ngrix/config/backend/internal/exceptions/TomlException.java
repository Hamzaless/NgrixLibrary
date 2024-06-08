package space.ngrix.config.backend.internal.exceptions;

import space.ngrix.config.backend.internal.exception.SimplixException;

/**
 * Thrown when a problem occurs during parsing or writing NBT data.
 */
public class TomlException extends SimplixException {

  private static final long serialVersionUID = 1L;

  public TomlException(final Throwable cause, final String... messages) {
    super(cause, messages);
  }

  public TomlException(final String... messages) {
    super(messages);
  }
}
