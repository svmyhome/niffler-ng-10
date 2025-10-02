package utils;

import com.github.javafaker.Faker;
import java.util.Locale;

public class DataGenerator {

  private static final Faker faker = new Faker(Locale.of("ru"));

  public static String getRandomCategoryName() {
    return faker.commerce().department();
  }
}
