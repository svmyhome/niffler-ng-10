package utils;

import com.github.javafaker.Faker;
import java.util.Locale;

public class RandomDataUtils {

  private static final Faker faker = new Faker(Locale.of("ru"));



  public static String randomFullName() {return faker.name().name();}
  public static String randomFirstName() {return  faker.name().firstName();}
  public static String randomLastName(){return  faker.name().lastName();}
  public static String randomCategoryName() {return faker.commerce().department();}
  public static String randomSentence(int wordsCount) {return faker.lorem().sentence(wordsCount);}


  public static void main(String[] args) {
    System.out.println("NAME " + randomFullName());
    System.out.println("NAME " + randomFirstName());
    System.out.println("LAST_NAME " + randomLastName());
    System.out.println("CATEGORY " + randomCategoryName());
    System.out.println("SENTENCE " + randomSentence(4));
  }
}
