package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static guru.qa.niffler.condition.StatConditions.color;
import static guru.qa.niffler.condition.StatConditions.statBubblesContains;
import static guru.qa.niffler.condition.StatConditions.statBubblesInAnyOrder;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
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

  private final SelenideElement chartImage;
  private final ElementsCollection spendingLegends;

//  public StatComponent() {
//    super($("#stat"));
//  }

    public StatComponent(SelenideDriver driver, SelenideElement self) {
        super(driver, driver.$("#stat"));
        this.chartImage = self.$("canvas[role='img']");
        this.spendingLegends = driver.$("#legend-container").$$("li");

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
    spendingLegends.should(statBubblesInAnyOrder(expectedBubbles));
    return this;
  }

  @Step("Check that spending contains from chart elements")
  public StatComponent checkBubblesContains(Bubble... expectedBubbles) {
    spendingLegends.should(statBubblesContains(expectedBubbles));
    return this;
  }
}
