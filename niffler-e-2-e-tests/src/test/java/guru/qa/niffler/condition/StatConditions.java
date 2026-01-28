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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
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

    @Nonnull
    public static WebElementsCondition color(Color... expectedColors) {
        return new WebElementsCondition() {
            private final String expectedRgba = Arrays.stream(expectedColors).map(c -> c.rgb).toList()
                    .toString();

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalArgumentException("No expected colors given");
                }
                if (expectedColors.length!=elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                            expectedColors.length, elements.size());
                    List<String> actualColors = elements.stream()
                            .map(el -> el.getCssValue("background-color"))
                            .toList();
                    return rejected(message, actualColors.toString());
                }
                boolean passed = true;
                List<String> actualRgbaList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color colorToCheck = expectedColors[i];
                    final String rgba = elementToCheck.getCssValue("background-color");
                    actualRgbaList.add(rgba);
                    if (passed) {
                        passed = colorToCheck.rgb.equals(rgba);
                    }
                }

                if (!passed) {
                    return StatConditions.createMismatchResult(expectedRgba, actualRgbaList);
                }
                return accepted();
            }

            @NotNull
            private CheckResult createMismatchResult(List<String> actualRgbaList) {
                final String actualRgba = actualRgbaList.toString();
                final String message = String.format("List colors mismatch (expected: %s, actual: %s)",
                        expectedRgba, actualRgba);
                return rejected(message, actualRgba);
            }

            @Override
            @Nonnull
            public String toString() {
                return expectedRgba;
            }
        };
    }

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
                    throw new IllegalArgumentException("No expected bubbles given");
                }
                if (expectedBubbles.length!=elements.size()) {
                    return bubblesMismatchResults(elements, expectedBubbles);
                }
                boolean passed = true;
                List<String> actualRgbaList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color colorToCheck = expectedBubbles[i].color();
                    final String descriptionToCheck = expectedBubbles[i].text();
                    final String rgba = elementToCheck.getCssValue("background-color");
                    final String actualDescription = elementToCheck.getText();
                    actualRgbaList.add(rgba + ": " + actualDescription);
                    if (passed) {
                        passed = colorToCheck.rgb.equals(rgba) && descriptionToCheck.equals(actualDescription);
                    }
                }
                if (!passed) {
                    return StatConditions.createMismatchResult(expectedResult, actualRgbaList);
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

    @Nonnull
    public static WebElementsCondition statBubblesInAnyOrder(Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            final Set<Bubble> expectedSet = new HashSet<>(Arrays.asList(expectedBubbles));

            private final String expectedResult = expectedSet.stream()
                    .map(bubble -> bubble.color().rgb + ": " + bubble.text())
                    .collect(Collectors.joining(", ", "[", "]"));

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }
                if (expectedBubbles.length!= elements.size()) {
                    return bubblesMismatchResults(elements, expectedBubbles);
                }

                Set<Bubble> actualSet = new HashSet<>();
                List<String> actualRgbaList = new ArrayList<>();
                setActualResult(actualSet, actualRgbaList, elements);
                if (expectedSet.equals(actualSet)) {
                    return accepted();
                } else {
                    return createMismatchResult(expectedResult, actualRgbaList);
                }
            }

            @Override
            @Nonnull
            public String toString() {
                return expectedResult;
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesContains(Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            final List<Bubble> expectedList = Arrays.asList(expectedBubbles);
            private final String expectedResult = expectedList.stream()
                    .map(bubble -> bubble.color().rgb + ": " + bubble.text())
                    .collect(Collectors.joining(", ", "[", "]"));

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }
                if (expectedBubbles.length > elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                            expectedBubbles.length, elements.size());
                    List<String> actualRgbaListErr = elements.stream()
                            .map(element ->
                                    element.getCssValue("background-color") + ": " + element.getText())
                            .toList();
                    return rejected(message, actualRgbaListErr.toString());
                }
                Set<Bubble> actualResultSet = new HashSet<>();
                List<String> actualRgbaList = new ArrayList<>();
                setActualResult(actualResultSet, actualRgbaList, elements);
                if (actualResultSet.containsAll(expectedList)) {
                    return accepted();
                } else {
                    return createMismatchResult(expectedResult, actualRgbaList);
                }
            }

            @Override
            @Nonnull
            public String toString() {
                return expectedResult;
            }
        };
    }

    private @Nonnull
    static CheckResult createMismatchResult(String expected, List<String> actual) {
        String actualString = actual.toString();
        String message = String.format("List mismatch (expected: %s, actual: %s)",
                expected, actualString);
        return rejected(message, actualString);
    }
    private @Nonnull
    static CheckResult bubblesMismatchResults(List<WebElement> elements, Bubble... expectedBubbles) {
        final String message = String.format("List size mismatch (expected: %s, actual: %s)",
                expectedBubbles.length, elements.size());
        List<String> actualRgbaListErr = elements.stream()
                .map(element ->
                        element.getCssValue("background-color") + ": " + element.getText())
                .toList();
        return rejected(message, actualRgbaListErr.toString());
    }

    private static void setActualResult(Set<Bubble> actualSet, List<String> actualRgbaList, List<WebElement> elements) {
        for (WebElement element : elements) {
            String rgba = element.getCssValue("background-color");
            String description = element.getText();
            actualSet.add(new Bubble(Color.fromRgb(rgba), description));
            actualRgbaList.add(rgba + ": " + description);
        }
    }
}