package space.ngrix.config.backend.internal.serialize;

import lombok.NonNull;

public interface SimplixSerializable<T> {

  /**
   * Get our serializable from data in data-structure.
   *
   * @param obj Data to deserialize our class from.
   * @throws ClassCastException Exception thrown when deserialization failed.
   */
  T deserialize(@NonNull final Object obj) throws ClassCastException;

  /**
   * Save our serializable to data-structure.
   *
   * @throws ClassCastException Exception thrown when serialization failed.
   */
  Object serialize(@NonNull final T t) throws ClassCastException;

  Class<T> getClazz();
}
