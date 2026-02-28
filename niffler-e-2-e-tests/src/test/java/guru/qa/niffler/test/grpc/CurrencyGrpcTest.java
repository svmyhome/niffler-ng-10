package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.CalculateResponse;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
import guru.qa.niffler.grpc.CurrencyValues;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CurrencyGrpcTest extends BaseGrpcTest {

    @Test
    @DisplayName("GRPC: Should return 4 currencies")
    void allCurrencyShouldReturned() {
        final CurrencyResponse allCurrencies = blockingStub.getAllCurrencies(Empty.getDefaultInstance());
        final List<Currency> allCurrenciesList = allCurrencies.getAllCurrenciesList();
        Assertions.assertEquals(4, allCurrenciesList.size());
    }

    @Test
    @DisplayName("GRPC: Should convert 100 USD to RUB")
    void shouldConvertUsdToRub() {
        final CalculateRequest request = CalculateRequest.newBuilder()
                .setSpendCurrency(CurrencyValues.USD)
                .setDesiredCurrency(CurrencyValues.RUB)
                .setAmount(100.0)
                .build();
        final CalculateResponse calculateResponse = blockingStub.calculateRate(request);
        final double calculatedAmount = calculateResponse.getCalculatedAmount();
        Assertions.assertEquals(6666.67, calculatedAmount);
    }

    @Test
    @DisplayName("GRPC: Should convert 100 KZT to EUR")
    void shouldConvertKztToEur() {
        final CalculateRequest request = CalculateRequest.newBuilder()
                .setSpendCurrency(CurrencyValues.KZT)
                .setDesiredCurrency(CurrencyValues.EUR)
                .setAmount(0.19)
                .build();
        final CalculateResponse calculateResponse = blockingStub.calculateRate(request);
        final double calculatedAmount = calculateResponse.getCalculatedAmount();
        Assertions.assertEquals(6666.67, calculatedAmount);
    }

    @Test
    @DisplayName("GRPC: Converting RUB to RUB should return the same amount")
    void shouldReturnSameAmountWhenConvertingSameCurrency() {
        CalculateRequest request = CalculateRequest.newBuilder()
                .setSpendCurrency(CurrencyValues.RUB)
                .setDesiredCurrency(CurrencyValues.RUB)
                .setAmount(10.0)
                .build();

        double result = blockingStub.calculateRate(request).getCalculatedAmount();
        Assertions.assertEquals(10.0, result);
    }


    @Test
    @DisplayName("GRPC: Should return 0 when converting zero amount")
    void shouldReturnZeroForZeroAmount() {
        CalculateRequest request = CalculateRequest.newBuilder()
                .setSpendCurrency(CurrencyValues.EUR)
                .setDesiredCurrency(CurrencyValues.USD)
                .setAmount(0.0)
                .build();

        double result = blockingStub.calculateRate(request).getCalculatedAmount();
        Assertions.assertEquals(0.0, result);
    }

    @Test
    @DisplayName("GRPC: Should handle negative amounts correctly")
    void shouldHandleNegativeAmount() {
        CalculateRequest request = CalculateRequest.newBuilder()
                .setSpendCurrency(CurrencyValues.USD)
                .setDesiredCurrency(CurrencyValues.RUB)
                .setAmount(-100.0)
                .build();

        double result = blockingStub.calculateRate(request).getCalculatedAmount();
        Assertions.assertTrue(result < 0, "Negative amount should return negative result");
    }
}
