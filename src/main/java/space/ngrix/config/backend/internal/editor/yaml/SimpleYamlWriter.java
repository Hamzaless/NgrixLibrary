package space.ngrix.config.backend.internal.editor.yaml;

import com.esotericsoftware.yamlbeans.YamlWriter;
import space.ngrix.config.backend.internal.provider.SimplixProviders;
import space.ngrix.config.backend.util.FileUtils;
import java.io.File;
import java.io.Writer;

/**
 * Enhanced Version of YamlWriter of EsotericSoftware, which implements {@link AutoCloseable}
 */
public class SimpleYamlWriter extends YamlWriter implements AutoCloseable {

  public SimpleYamlWriter(final Writer writer) {
    super(writer, SimplixProviders.yamlConfig());
  }

  public SimpleYamlWriter(final File file) {
    this(FileUtils.createWriter(file));
  }
}
