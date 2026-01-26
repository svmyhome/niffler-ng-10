package guru.qa.niffler.jupiter.extension;


import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.Browsers.FIREFOX;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.model.Browser;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.support.FieldContext;


public class BrowserConverter implements ArgumentConverter {

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        Browser browser = (Browser) source;
        String browserName = browser.name();
        if (browserName.contains(CHROME)) {
            browserName = CHROME.toLowerCase();
        } else if (browserName.contains(FIREFOX)) {
            browserName = FIREFOX.toLowerCase();
        }
        SelenideConfig config = new SelenideConfig()
                .browser(browserName)
                .pageLoadStrategy("eager")
                .timeout(6000L);
        return new SelenideDriver(config);
    }

    @Override
    public Object convert(Object source, FieldContext context) throws ArgumentConversionException {
        Browser browser = (Browser) source;
        SelenideConfig config = new SelenideConfig()
                .browser(browser.name().toLowerCase());

        return new SelenideDriver(config);
    }
}
