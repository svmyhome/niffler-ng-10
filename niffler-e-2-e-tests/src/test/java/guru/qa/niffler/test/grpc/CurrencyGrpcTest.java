package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CurrencyGrpcTest extends BaseGrpcTest{

    @Test
    void allCurrencyShouldReturned() {
        final CurrencyResponse allCurrencies = blockingStub.getAllCurrencies(Empty.getDefaultInstance());
        final List<Currency> allCurrenciesList = allCurrencies.getAllCurrenciesList();
        Assertions.assertEquals(4, allCurrenciesList.size());
    }
}
