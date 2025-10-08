package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.SpendingExtension;
import guru.qa.niffler.model.CurrencyValues;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(SpendingExtension.class)
public @interface Spending {

  String username();

  String category();

  String description();

  double amount();

  CurrencyValues currency();
}
