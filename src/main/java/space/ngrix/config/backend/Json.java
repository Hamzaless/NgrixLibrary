package space.ngrix.config.backend;

import space.ngrix.config.backend.internal.FileData;
import space.ngrix.config.backend.internal.FileType;
import space.ngrix.config.backend.internal.FlatFile;
import space.ngrix.config.backend.internal.settings.ReloadSettings;
import space.ngrix.config.backend.util.FileUtils;
import lombok.Cleanup;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public class Json extends FlatFile {

  public Json(final Json json) {
    super(json.getFile(), json.fileType);
    this.fileData = json.getFileData();
    this.pathPrefix = json.getPathPrefix();
  }

  public Json(final String name, final String path) {
    this(name, path, null);
  }

  public Json(final String name, final String path, final InputStream inputStream) {
    this(name, path, inputStream, null);
  }

  public Json(final String name,
              @Nullable final String path,
              @Nullable final InputStream inputStream,
              @Nullable final ReloadSettings reloadSettings) {
    this(name, path, inputStream, reloadSettings, null);
  }

  public Json(final String name,
              @Nullable final String path,
              @Nullable final InputStream inputStream,
              @Nullable final ReloadSettings reloadSettings,
              @Nullable final Consumer<FlatFile> reloadConsumer) {
    super(name, path, FileType.JSON, reloadConsumer);

    if ((create() || this.file.length() == 0) && inputStream != null) {
      FileUtils.writeToFile(this.file, inputStream);
    }

    if (reloadSettings != null) {
      this.reloadSettings = reloadSettings;
    }
    forceReload();
  }

  public Json(final File file) {
    super(file, FileType.JSON);
    create();
    forceReload();
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to override (Points where JSON is unspecific for typical FlatFiles)
  // ----------------------------------------------------------------------------------------------------

  /**
   * Gets a Map by key Although used to get nested objects {@link Json}
   *
   * @param key Path to Map-List in JSON
   * @return Map
   */
  @Override
  public final Map<?,?> getMap(final String key) {
    val finalKey = (this.pathPrefix == null) ? key : this.pathPrefix + "." + key;
    if (!contains(finalKey)) {
      return new HashMap<>();
    } else {
      val map = get(key);
      if (map instanceof Map) {
        return (Map<?, ?>) this.fileData.get(key);
      } else if (map instanceof JSONObject) {
        return ((JSONObject) map).toMap();
      }
      // Exception in casting
      throw new IllegalArgumentException(
              "ClassCastEx: Json contains key: '" + key + "' but it is not a Map");
    }
  }

  // ----------------------------------------------------------------------------------------------------
  // Abstract methods to implement
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected final Map<String, Object> readToMap() throws IOException {
    if (this.file.length() == 0) {
      Files.write(this.file.toPath(), Collections.singletonList("{}"));
    }

    val jsonTokener = new JSONTokener(FileUtils.createInputStream(this.file));
    return new JSONObject(jsonTokener).toMap();
  }

  @Override
  protected final void write(final FileData data) throws IOException {
    @Cleanup val writer = FileUtils.createWriter(this.file);
    writer.write(data.toJsonObject().toString(3));
    writer.flush();
  }
}
