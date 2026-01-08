package guru.qa.niffler.data.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataFilterValues {
  ALL("All"),
  MONTH("Last month"),
  WEEK("Last week"),
  TODAY("Today");

  private final String period;
}
