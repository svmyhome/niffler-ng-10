package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.condition.StatConditions.color;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Color;
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
  private final ElementsCollection spendingLegends = $$("#legend-container li");

  public StatComponent() {
    super($("#stat"));
  }


  @Step("Spending legend should have'{description}'")
  public @Nonnull StatComponent checkThatSpendingsLengendContains(String description) {
    spendingLegends.find(text(description)).should(visible);
    return this;
  }

  @Step("Get screenshot")
  public @Nonnull BufferedImage chartScreenShot() throws IOException {
    return ImageIO.read(Objects.requireNonNull(chartImage.screenshot()));
  }

  @Step("Check bubbles")
  public StatComponent checkBubbles(Color... expectedColors) {
    spendingLegends.should(color(expectedColors));
    return this;
  }

}
