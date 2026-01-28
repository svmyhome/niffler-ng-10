package guru.qa.niffler.jupiter.converter;


import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.support.FieldContext;


public class BrowserConverter implements ArgumentConverter {

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        Browser browser = (Browser) source;
        return new SelenideDriver(browser.config);
    }

    @Override
    public Object convert(Object source, FieldContext context) throws ArgumentConversionException {
        Browser browser = (Browser) source;
        final SelenideConfig config = new SelenideConfig()
                .browser(browser.name().toLowerCase());

        return new SelenideDriver(config);
    }
}
