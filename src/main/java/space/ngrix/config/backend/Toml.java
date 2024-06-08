package space.ngrix.config.backend;

import space.ngrix.config.backend.internal.FileData;
import space.ngrix.config.backend.internal.FileType;
import space.ngrix.config.backend.internal.FlatFile;
import space.ngrix.config.backend.internal.editor.toml.TomlManager;
import space.ngrix.config.backend.internal.settings.ReloadSettings;
import space.ngrix.config.backend.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class Toml extends FlatFile {

  public Toml(@NonNull final Toml toml) {
    super(toml.getFile());
    this.fileData = toml.getFileData();
    this.pathPrefix = toml.getPathPrefix();
  }

  public Toml(final String name, final String path) {
    this(name, path, null);
  }

  public Toml(final String name, final String path, final InputStream inputStream) {
    this(name, path, inputStream, null, null);
  }

  public Toml(
      @NonNull final String name,
      @NonNull final String path,
      @Nullable final InputStream inputStream,
      @Nullable final ReloadSettings reloadSettings,
      @Nullable final Consumer<FlatFile> reloadConsumer
  ) {
    super(name, path, FileType.TOML, reloadConsumer);

    if (create() && inputStream != null) {
      FileUtils.writeToFile(this.file, inputStream);
    }

    if (reloadSettings != null) {
      this.reloadSettings = reloadSettings;
    }

    forceReload();
  }

  public Toml(final File file) {
    super(file, FileType.TOML);
    create();
    forceReload();
  }

  // ----------------------------------------------------------------------------------------------------
  // Abstract methods to implement
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected final Map<String, Object> readToMap() throws IOException {
    return TomlManager.read(getFile());
  }

  @Override
  protected final void write(final FileData data) {
    try {
      TomlManager.write(data.toMap(), getFile());
    } catch (final IOException ioException) {
      System.err.println("Exception while writing fileData to file '" + getName() + "'");
      System.err.println("In '" + FileUtils.getParentDirPath(this.file) + "'");
      ioException.printStackTrace();
    }
  }
}
