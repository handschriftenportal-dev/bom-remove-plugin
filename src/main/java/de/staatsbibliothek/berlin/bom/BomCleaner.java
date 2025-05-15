package de.staatsbibliothek.berlin.bom;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Piotr.Czarnecki@sbb.spk-berlin.de
 * @since 11.02.22
 */
public class BomCleaner {

  private static Logger logger = Logger.getLogger(BomCleaner.class.getName());

  private static final byte BYTE_FF = (byte) 0xFF;
  private static final byte BYTE_FE = (byte) 0xFE;
  private static final byte BYTE_00 = (byte) 0x00;
  private static final byte BYTE_EF = (byte) 0xEF;
  private static final byte BYTE_BB = (byte) 0xBB;
  private static final byte BYTE_BF = (byte) 0xBF;

  public boolean processDirectory(Path src, Path dst, boolean recursive, boolean ignoreErrors) throws IOException {

    final Path source = src.toAbsolutePath();
    final Path target = dst.toAbsolutePath();
    Files.createDirectories(target);
    Files.walkFileTree(source,
        new SimpleFileVisitor<Path>() {

          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (recursive) {
              Files.createDirectories(target.resolve(source.relativize(dir)));
              return FileVisitResult.CONTINUE;
            }
            return FileVisitResult.SKIP_SUBTREE;
          }

          @Override
          public FileVisitResult visitFileFailed(Path file, IOException io) {
            if (ignoreErrors) {
              return FileVisitResult.SKIP_SUBTREE;
            }
            logger.log(Level.SEVERE, "Error is occurred", io);
            return FileVisitResult.TERMINATE;
          }

          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            BomCleaner.copyFile(file, target.resolve(source.relativize(file)));
            return FileVisitResult.CONTINUE;
          }
        });
    return true;
  }

  public static void copyFile(Path srcFile, Path dstFile) throws IOException {
    try (FileChannel inputChannel = FileChannel.open(srcFile, StandardOpenOption.READ);
        RandomAccessFile writer = new RandomAccessFile(dstFile.toFile(), "rw");
        FileChannel outputChannel = writer.getChannel();) {
      long offset = getOffset(srcFile);
      logger.info("Copy " + srcFile + " to " + dstFile + " (BOM Offset: " + offset + ")");
      if (offset == 0L) {
        outputChannel.transferFrom(inputChannel, 0L, Long.MAX_VALUE);
      } else {
        outputChannel.transferFrom(inputChannel.position(offset), 0L, Long.MAX_VALUE);
      }
    }
  }

  public static long getOffset(Path srcFile) throws IOException {
    try (FileChannel inputChannel = FileChannel.open(srcFile, StandardOpenOption.READ)) {
      ByteBuffer bomBB = ByteBuffer.allocate(4);
      inputChannel.read(bomBB);
      byte[] bom = bomBB.array();
      long offset = 0L;
      if (bom[0] == BYTE_FF
          && bom[1] == BYTE_FE
          && bom[2] == BYTE_00
          && bom[3] == BYTE_00) {
        offset = 4L;
      } else if (bom[0] == BYTE_00
          && bom[1] == BYTE_00
          && bom[2] == BYTE_FE
          && bom[3] == BYTE_FF) {
        offset = 4L;
      } else if (bom[0] == BYTE_EF
          && bom[1] == BYTE_BB
          && bom[2] == BYTE_BF) {
        offset = 3L;
      } else if (bom[0] == BYTE_FF
          && bom[1] == BYTE_FE) {
        offset = 2L;
      } else if (bom[0] == BYTE_FE
          && bom[1] == BYTE_FF) {
        offset = 2L;
      }
      return offset;
    }
  }

}
