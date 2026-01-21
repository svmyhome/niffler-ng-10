package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.color;
import static guru.qa.niffler.condition.StatConditions.colorAnyOrder;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.spend.Bubble;
import io.qameta.allure.Step;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

@ParametersAreNonnullByDefault
public class StatComponent extends BaseComponent<StatComponent> {

  private final SelenideElement chartImage = self.$("canvas[role='img']");
  private final ElementsCollection spendingLegends = $("#legend-container").$$("li");

  public StatComponent() {
    super($("#stat"));
  }

  @Step("Spending legend should have'{description}'")
  public @Nonnull StatComponent checkThatSpendingsLengendContains(String description) {
    spendingLegends.find(text(description)).should(visible);
    return this;
  }

  @Step("Get screenshot of spending chart for comparison")
  public @Nonnull BufferedImage getChartScreenshot() {
    try {
      return ImageIO.read(Objects.requireNonNull(chartImage.screenshot()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Step("Check that spending chart elements have colors: {expectedColors}")
  public StatComponent checkOnlyColorBubbles(Bubble... expectedColors) {
    spendingLegends.should(color(expectedColors));
    return this;
  }

  @Step("Check that spending chart elements have colors and text")
  public StatComponent checkBubbles(Bubble... expectedBubbles) {
    spendingLegends.should(color(expectedBubbles));
    return this;
  }

  @Step("Check that spending chart elements have colors and text")
  public StatComponent checkBubblesInAnyOrder(Bubble... expectedBubbles) {
    spendingLegends.should(colorAnyOrder(expectedBubbles));
    return this;
  }
}
