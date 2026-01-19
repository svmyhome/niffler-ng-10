package utils;

import guru.qa.niffler.jupiter.extension.ScreenShotTestExtension;
import java.awt.image.BufferedImage;
import java.util.function.BooleanSupplier;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

public class ScreenDiffResult implements BooleanSupplier {

  private final BufferedImage expected;
  private final BufferedImage actual;
  private final ImageDiff diff;
  private final boolean hasDiff;
  private static int ALLOWED_DIFF_PIXELS;

  public ScreenDiffResult(BufferedImage expected, BufferedImage actual) {
    this.expected = expected;
    this.actual = actual;
    this.diff = new ImageDiffer().makeDiff(expected, actual);
    switch (System.getProperty("os.name")) {
      case "Mac OS X":
        ALLOWED_DIFF_PIXELS = 200;
        break;
      case "Windows":
        ALLOWED_DIFF_PIXELS = 100;
        break;
      default:
        ALLOWED_DIFF_PIXELS = 50;
    }
    this.hasDiff = diff.getDiffSize() > ALLOWED_DIFF_PIXELS;
  }

  @Override
  public boolean getAsBoolean() {
    if (hasDiff) {
      ScreenShotTestExtension.setExpected(expected);
      ScreenShotTestExtension.setActual(actual);
      ScreenShotTestExtension.setDiff(diff.getMarkedImage());
    }
    return hasDiff;
  }
}
