package guru.qa.niffler.condition;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;
import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.spend.Bubble;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@ParametersAreNonnullByDefault
public class StatConditions {

  @Nonnull
  public static WebElementCondition color(Color expectedColor) {
    return new WebElementCondition("color") {
      @NotNull
      @Override
      public CheckResult check(Driver driver, WebElement element) {
        final String rgba = element.getCssValue("background-color");
        return new CheckResult(
            expectedColor.rgb.equals(rgba),
            rgba
        );
      }
    };
  }

//  @Nonnull
//  public static WebElementsCondition color(Color... expectedColors) {
//    return new WebElementsCondition() {
//      private final String expectedRgba = Arrays.stream(expectedColors).map(c -> c.rgb).toList()
//          .toString();
//
//      @Nonnull
//      @Override
//      public CheckResult check(Driver driver, List<WebElement> elements) {
//        if (ArrayUtils.isEmpty(expectedColors)) {
//          throw new IllegalArgumentException("No expected colors given");
//        }
//        if (expectedColors.length != elements.size()) {
//          final String message = String.format("List size mismatch (expected: %s, actual: %s)",
//              expectedColors.length, elements.size());
//          List<String> actualColors = elements.stream()
//              .map(el -> el.getCssValue("background-color"))
//              .toList();
//          return rejected(message, actualColors.toString());
//        }
//        boolean passed = true;
//        List<String> actualRgbaList = new ArrayList<>();
//        for (int i = 0; i < elements.size(); i++) {
//          final WebElement elementToCheck = elements.get(i);
//          final Color colorToCheck = expectedColors[i];
//          final String rgba = elementToCheck.getCssValue("background-color");
//          actualRgbaList.add(rgba);
//          if (passed) {
//            passed = colorToCheck.rgb.equals(rgba);
//          }
//        }
//
//        if (!passed) {
//          final String actualRgba = actualRgbaList.toString();
//          final String message = String.format("List colors mismatch (expected: %s, actual: %s)",
//              expectedRgba, actualRgba);
//          return rejected(message, actualRgba);
//        }
//        return accepted();
//      }
//
//      @Override
//      @Nonnull
//      public String toString() {
//        return expectedRgba;
//      }
//    };
//  }

  @Nonnull
  public static WebElementsCondition color(Bubble... expectedBubbles) {
    return new WebElementsCondition() {

      private final String expectedResult = Arrays.stream(expectedBubbles)
          .map(bubble -> bubble.color().rgb + ": " + bubble.text())
          .collect(Collectors.joining(", ", "[", "]"));

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (ArrayUtils.isEmpty(expectedBubbles)) {
          throw new IllegalArgumentException("No expected colors given");
        }
        if (expectedBubbles.length != elements.size()) {
          final String message = String.format("List size mismatch (expected: %s, actual: %s)",
              expectedBubbles.length, elements.size());
          List<String> actualColors = elements.stream()
              .map(el -> el.getCssValue("background-color"))
              .toList();
          return rejected(message, actualColors.toString());
        }
        boolean passed = true;
        List<String> actualRgbaList = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
          final WebElement elementToCheck = elements.get(i);
          final Color colorToCheck = expectedBubbles[i].color();
          final String descriptionToCheck = expectedBubbles[i].text();
          final String rgba = elementToCheck.getCssValue("background-color");
          List<WebElement> foundElements = elementToCheck.findElements(
              By.xpath(String.format("//li[contains(text(),'%s')]", descriptionToCheck))
          );
          String actualDescription = "";
          if (!foundElements.isEmpty()) {
            actualDescription = foundElements.getFirst().getText();
          }
          actualRgbaList.add(rgba + ": " + actualDescription);
          if (passed) {
            passed = colorToCheck.rgb.equals(rgba) && descriptionToCheck.equals(actualDescription);
          }
        }

        if (!passed) {
          final String actualRgba = actualRgbaList.toString();
          final String message = String.format("List colors mismatch (expected: %s, actual: %s)",
              expectedResult, actualRgba);
          return rejected(message, actualRgba);
        }
        return accepted();
      }

      @Override
      @Nonnull
      public String toString() {
        return expectedResult;
      }
    };
  }

}
