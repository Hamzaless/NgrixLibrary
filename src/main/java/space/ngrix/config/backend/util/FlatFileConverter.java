package space.ngrix.config.backend.util;

import space.ngrix.config.backend.internal.FlatFile;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FlatFileConverter {

  public void addAllData(final FlatFile source, final FlatFile destination) {
    destination.getFileData().clear();
    destination.getFileData().loadData(source.getFileData().toMap());
    destination.write();
  }
}
