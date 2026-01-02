package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import java.lang.reflect.Field;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class SpendClientInjector implements TestInstancePostProcessor {

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context)
      throws Exception {
    for (Field field : testInstance.getClass().getDeclaredFields()) {
      if (field.getType().isAssignableFrom(SpendClient.class)) {
        field.setAccessible(true);
        field.set(testInstance, new SpendDbClient());
      }
    }
  }
}
