package guru.qa.niffler.assertions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import io.qameta.allure.Step;
import java.awt.image.BufferedImage;
import utils.ScreenDiffResult;

public class ImageAssertions {

  @Step("Check that screenshot matches expected image")
  public static void checkScreenshotMatches(BufferedImage expected, BufferedImage actual) {
    assertFalse(new ScreenDiffResult(
            expected,
            actual
        ),
        "Screenshot comparison failure");
  }
}
