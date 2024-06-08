package space.ngrix.config.backend;

import space.ngrix.config.backend.internal.FileData;
import space.ngrix.config.backend.internal.FileType;
import space.ngrix.config.backend.internal.FlatFile;
import space.ngrix.config.backend.internal.editor.yaml.SimpleYamlReader;
import space.ngrix.config.backend.internal.editor.yaml.SimpleYamlWriter;
import space.ngrix.config.backend.internal.editor.yaml.YamlEditor;
import space.ngrix.config.backend.internal.editor.yaml.YamlParser;
import space.ngrix.config.backend.internal.settings.ConfigSettings;
import space.ngrix.config.backend.internal.settings.DataType;
import space.ngrix.config.backend.internal.settings.ReloadSettings;
import space.ngrix.config.backend.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import lombok.*;
import org.jetbrains.annotations.Nullable;

@Getter
public class Yaml extends FlatFile {

  protected final InputStream inputStream;
  protected final YamlEditor yamlEditor;
  protected final YamlParser parser;
  @Setter
  private ConfigSettings configSettings = ConfigSettings.SKIP_COMMENTS;

  public Yaml(@NonNull final Yaml yaml) {
    super(yaml.getFile());
    this.fileData = yaml.getFileData();
    this.yamlEditor = yaml.getYamlEditor();
    this.parser = yaml.getParser();
    this.configSettings = yaml.getConfigSettings();
    this.inputStream = yaml.getInputStream().orElse(null);
    this.pathPrefix = yaml.getPathPrefix();
    this.reloadConsumer = yaml.getReloadConsumer();
  }

  public Yaml(final String name, @Nullable final String path) {
    this(name, path, null, null, null, null);
  }

  public Yaml(final String name,
              @Nullable final String path,
              @Nullable final InputStream inputStream) {
    this(name, path, inputStream, null, null, null);
  }

  public Yaml(final String name,
              @Nullable final String path,
              @Nullable final InputStream inputStream,
              @Nullable final ReloadSettings reloadSettings,
              @Nullable final ConfigSettings configSettings,
              @Nullable final DataType dataType) {
    this(name, path, inputStream, reloadSettings, configSettings, dataType, null);
  }

  public Yaml(final String name,
              @Nullable final String path,
              @Nullable final InputStream inputStream,
              @Nullable final ReloadSettings reloadSettings,
              @Nullable final ConfigSettings configSettings,
              @Nullable final DataType dataType,
              @Nullable final Consumer<FlatFile> reloadConsumer) {
    super(name, path, FileType.YAML, reloadConsumer);
    this.inputStream = inputStream;

    if (create() && inputStream != null) {
      FileUtils.writeToFile(this.file, inputStream);
    }

    this.yamlEditor = new YamlEditor(this.file);
    this.parser = new YamlParser(this.yamlEditor);

    if (reloadSettings != null) {
      this.reloadSettings = reloadSettings;
    }

    if (configSettings != null) {
      this.configSettings = configSettings;
    }

    if (dataType != null) {
      this.dataType = dataType;
    } else {
      this.dataType = DataType.forConfigSetting(configSettings);
    }

    forceReload();
  }

  public Yaml(final File file) {
    this(file.getName(), FileUtils.getParentDirPath(file));
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to override (Points where YAML is unspecific for typical FlatFiles)
  // ----------------------------------------------------------------------------------------------------

  public Yaml addDefaultsFromInputStream() {
    return addDefaultsFromInputStream(getInputStream().orElse(null));
  }

  public Yaml addDefaultsFromInputStream(@Nullable final InputStream inputStream) {
    reloadIfNeeded();
    // Creating & setting defaults
    if (inputStream == null) {
      return this;
    }

    try {
      val data = new SimpleYamlReader(
              new InputStreamReader(inputStream, StandardCharsets.UTF_8)).readToMap();

      val newData = new FileData(data, DataType.UNSORTED);

      for (val key : newData.keySet()) {
        if (!this.fileData.containsKey(key)) {
          this.fileData.insert(key, newData.get(key));
        }
      }

      write();
    } catch (final Exception ex) {
      ex.printStackTrace();
    }

    return this;
  }

  // ----------------------------------------------------------------------------------------------------
  // Abstract methods to implement
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected Map<String, Object> readToMap() throws IOException {
    @Cleanup val reader = new SimpleYamlReader(getFile());
    return reader.readToMap();
  }

  @Override
  protected void write(final FileData data) throws IOException {
    // If Comments shouldn't be preserved
    if (!ConfigSettings.PRESERVE_COMMENTS.equals(this.configSettings)) {
      write0(this.fileData);
      return;
    }

    val unEdited = this.yamlEditor.read();
    write0(this.fileData);
    this.yamlEditor.write(this.parser.parseLines(unEdited, this.yamlEditor.readKeys()));
  }

  // Writing without comments
  private void write0(final FileData fileData) throws IOException {
    @Cleanup val writer = new SimpleYamlWriter(this.file);
    writer.write(fileData.toMap());
  }

  // ----------------------------------------------------------------------------------------------------
  // Specific utility methods for YAML
  // ----------------------------------------------------------------------------------------------------

  public final List<String> getHeader() {
    return this.yamlEditor.readHeader();
  }

  public final void setHeader(final List<String> header) {
    this.yamlEditor.setHeader(header);
  }

  public final void setHeader(final String... header) {
    setHeader(Arrays.asList(header));
  }

  public final void addHeader(final List<String> toAdd) {
    this.yamlEditor.addHeader(toAdd);
  }

  public final void addHeader(final String... header) {
    addHeader(Arrays.asList(header));
  }

  public final void framedHeader(final String... header) {
    List<String> stringList = new ArrayList<>();
    var border = "# +----------------------------------------------------+ #";
    stringList.add(border);

    for (var line : header) {
      var builder = new StringBuilder();
      if (line.length() > 50) {
        continue;
      }

      int length = (50 - line.length()) / 2;
      var finalLine = new StringBuilder(line);

      for (int i = 0; i < length; i++) {
        finalLine.append(" ");
        finalLine.reverse();
        finalLine.append(" ");
        finalLine.reverse();
      }

      if (line.length() % 2 != 0) {
        finalLine.append(" ");
      }

      builder.append("# < ").append(finalLine).append(" > #");
      stringList.add(builder.toString());
    }
    stringList.add(border);
    setHeader(stringList);
  }

  public final Optional<InputStream> getInputStream() {
    return Optional.ofNullable(this.inputStream);
  }
}
