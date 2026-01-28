package guru.qa.niffler.jupiter.converter;

import com.codeborne.selenide.SelenideConfig;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Browser {

    CHROME(new SelenideConfig()
            .browser("chrome")
            .pageLoadStrategy("eager")
            .timeout(6000L)),
    FIREFOX(new SelenideConfig()
            .browser("firefox")
            .pageLoadStrategy("eager")
            .timeout(6000L));

    public final SelenideConfig config;
}
