package guru.qa.niffler.model.spend;


import java.util.Arrays;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CurrencyValues {
  RUB("₽"), USD("$"), EUR("€"), KZT("₸");
  public final String currency;

  public static CurrencyValues fromCurrency(String currency) {
    return Arrays.stream(CurrencyValues.values()).filter(e -> e.currency.equals(currency)).findFirst().orElse(null);
  }

}