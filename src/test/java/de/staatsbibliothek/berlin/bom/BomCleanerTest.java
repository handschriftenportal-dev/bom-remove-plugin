package de.staatsbibliothek.berlin.bom;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 21.02.22
 */
class BomCleanerTest {

  @Test
  void testProcessDirectory() throws IOException {
    Path teiDirPath = Paths
        .get("src", "test", "resources", "tei");

    Path destination = Paths.get("target", "destTest");
    BomCleaner bomCleaner = new BomCleaner();
    bomCleaner.processDirectory(teiDirPath, destination, true, true);

    assertEquals(3L, BomCleaner.getOffset(teiDirPath.resolve("Berlin_test.xml")));
    assertEquals(0L, BomCleaner.getOffset(destination.resolve("Berlin_test.xml")));
  }

}
