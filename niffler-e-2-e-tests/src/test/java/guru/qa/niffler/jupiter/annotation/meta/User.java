package guru.qa.niffler.jupiter.annotation.meta;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendingExtension;
import guru.qa.niffler.jupiter.extension.UserExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@ExtendWith({
    UserExtension.class,
    CategoryExtension.class,
    SpendingExtension.class
})
public @interface User {

  String username() default "";

  int incomeInvitations() default 0;

  int outcomeInvitations() default 0;

  int friends() default 0;

  Category[] categories() default {};

  Spending[] spendings() default {};
}
