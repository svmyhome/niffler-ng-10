package guru.qa.niffler.model.gw.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.spend.CurrencyValues;
import java.util.List;

public record StatisticV2Json(
    @JsonProperty("total")
    Double total,
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("statByCategories")
    List<SumByCategory> statByCategories) {

}
