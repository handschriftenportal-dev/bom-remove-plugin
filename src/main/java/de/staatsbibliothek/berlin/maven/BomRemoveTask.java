package de.staatsbibliothek.berlin.maven;

import de.staatsbibliothek.berlin.bom.BomCleaner;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 11.02.22
 */
@Mojo(name = "BomRemove", defaultPhase = LifecyclePhase.PACKAGE)
public class BomRemoveTask extends AbstractMojo {

  private static Logger logger = Logger.getLogger(BomRemoveTask.class.getName());

  @Parameter(property = "source", defaultValue = ".")
  private String source;

  @Parameter(property = "destination", defaultValue = ".")
  private String destination;

  @Parameter(property = "recursive", defaultValue = "true")
  private boolean recursive;

  @Parameter(property = "ignoreErrors", defaultValue = "true")
  private boolean ignoreErrors;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    logger.info(
        "Processing from root directory: '" + source + "' to directory '" + destination + "' recursive: " + recursive
            + "' ignoreErrors: " + ignoreErrors);
    Path srcPath = Path.of(source);
    Path dstPath = Path.of(destination);
    BomCleaner bomCleaner = new BomCleaner();
    try {
      bomCleaner.processDirectory(srcPath, dstPath, recursive, ignoreErrors);
    } catch (IOException e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }
}
