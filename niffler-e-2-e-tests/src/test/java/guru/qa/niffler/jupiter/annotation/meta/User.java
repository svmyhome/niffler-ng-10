package guru.qa.niffler.jupiter.annotation.meta;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface User {

  String username() default "";

  int incomeInvitations() default 0;

  int outcomeInvitations() default 0;

  int friends() default 0;

  Category[] categories() default {};

  Spending[] spendings() default {};
}
