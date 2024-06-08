package space.ngrix.config.backend.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.val;

/**
 * Class to work with the parts of the lines of a YAML-file
 */
@UtilityClass
public class YamlUtils {

  public List<String> getCommentsFromLines(final List<String> lines) {
    final List<String> result = new ArrayList<>();

    for (val line : lines) {
      if (line.startsWith("#")) {
        result.add(line);
      }
    }
    return result;
  }

  public List<String> getFooterFromLines(final List<String> lines) {
    final List<String> result = new ArrayList<>();
    Collections.reverse(lines);
    for (final String line : lines) {
      if (!line.startsWith("#")) {
        Collections.reverse(result);
        return result;
      }
      result.add(line);
    }
    Collections.reverse(result);
    return result;
  }

  public List<String> getHeaderFromLines(final List<String> lines) {
    final List<String> result = new ArrayList<>();

    for (val line : lines) {
      if (!line.startsWith("#")) {
        return result;
      }
      result.add(line);
    }
    return result;
  }

  /**
   * @return List of comments that don't belong to header or footer
   */
  public List<String> getPureCommentsFromLines(final List<String> lines) {
    val comments = getCommentsFromLines(lines);
    val header = getHeaderFromLines(lines);
    val footer = getFooterFromLines(lines);

    comments.removeAll(header);
    comments.removeAll(footer);

    return comments;
  }

  public List<String> getLinesWithoutFooterAndHeaderFromLines(final List<String> lines) {
    val header = getHeaderFromLines(lines);
    val footer = getFooterFromLines(lines);

    lines.removeAll(header);
    lines.removeAll(footer);

    return lines;
  }

  public List<String> getKeys(final List<String> lines) {
    final List<String> result = new ArrayList<>();

    for (val line : lines) {
      if (!line.trim().startsWith("#")) {
        result.add(line);
      }
    }
    return result;
  }
}
