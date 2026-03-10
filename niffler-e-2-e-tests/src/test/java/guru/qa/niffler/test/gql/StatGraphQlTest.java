package guru.qa.niffler.test.gql;

import static io.qameta.allure.Allure.step;
import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.CategoriesQuery;
import guru.qa.StatQuery;
import guru.qa.StatQuery.StatByCategory;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.user.UserJson;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StatGraphQlTest extends BaseGraphQlTest {

    @Test
    @User
    @ApiLogin
    @DisplayName("GQL: Should return total price")
    void allStatShouldBeReturnedFromGateway(@Token String token) {
        ApolloCall<StatQuery.Data> statCall = apolloClient.query(StatQuery.builder()
                .filterCurrency(null)
                .statCurrency(null)
                .filterPeriod(null)
                .build()).addHttpHeader("authorization", token);
        ApolloResponse<StatQuery.Data> apolloResponse = Rx2Apollo.single(statCall).blockingGet();

        final StatQuery.Data data = apolloResponse.dataOrThrow();

        StatQuery.Stat stat = data.stat;

        step("Total price should be 0.0", () ->
                Assertions.assertEquals(
                        0.0, stat.total
                )
        );

    }

    @Test
    @User(
            spendings = {@Spending(
                    category = "Машина",
                    amount = 89900,
                    currency = CurrencyValues.RUB,
                    description = "Обучение Niffler 2.0 юбилейный поток!"
            )}
    )
    @ApiLogin
    @DisplayName("GQL: Should return spending")
    void spendingStatShouldBeReturnedFromGateway(@Token String token, UserJson user) {
        final SpendJson spend = user.testData().spendings().getFirst();
        ApolloCall<StatQuery.Data> statCall = apolloClient.query(StatQuery.builder()
                .filterCurrency(null)
                .statCurrency(null)
                .filterPeriod(null)
                .build()).addHttpHeader("authorization", token);
        ApolloResponse<StatQuery.Data> apolloResponse = Rx2Apollo.single(statCall).blockingGet();

        final StatQuery.Data data = apolloResponse.dataOrThrow();
        StatQuery.Stat stat = data.stat;

        step("Verify that total stat sum equals spending amount", () ->
                Assertions.assertEquals(spend.amount(), stat.total)
        );
        step("Verify that category name in stat equals spending category name", () ->
                Assertions.assertEquals(spend.category().name(), stat.statByCategories.getFirst().categoryName)
        );
        step("Verify that currency in stat equals spending currency", () ->
                Assertions.assertEquals(spend.currency().name(), stat.statByCategories.getFirst().currency.rawValue)
        );
        step("Verify that category sum equals spending amount", () ->
                Assertions.assertEquals(spend.amount(), stat.statByCategories.getFirst().sum)
        );
    }


    @Test
    @User(
            spendings = {
                    @Spending(
                            category = "Машина",
                            amount = 89900,
                            currency = guru.qa.niffler.model.spend.CurrencyValues.RUB,
                            description = "Обучение Niffler 2.0 юбилейный поток!"
                    ),
                    @Spending(
                            category = "Бег",
                            amount = 100,
                            currency = guru.qa.niffler.model.spend.CurrencyValues.USD,
                            description = "Кросовки"
                    ),
                    @Spending(
                            category = "Учеба",
                            amount = 200,
                            currency = guru.qa.niffler.model.spend.CurrencyValues.EUR,
                            description = "Курс"
                    ),
                    @Spending(
                            category = "Отдых",
                            amount = 300,
                            currency = guru.qa.niffler.model.spend.CurrencyValues.KZT,
                            description = "Пиво"
                    )
            }
    )
    @ApiLogin
    @DisplayName("GQL: Should return all spending")
    void allSpendingStatShouldBeReturnedFromGateway(@Token String token, UserJson user) {
        final List<SpendJson> spendingsTestData = user.testData().spendings();

        ApolloCall<StatQuery.Data> statCall = apolloClient.query(StatQuery.builder()
                .filterCurrency(null)
                .statCurrency(guru.qa.type.CurrencyValues.RUB)
                .filterPeriod(null)
                .build()).addHttpHeader("authorization", token);
        ApolloResponse<StatQuery.Data> apolloResponse = Rx2Apollo.single(statCall).blockingGet();

        final StatQuery.Data data = apolloResponse.dataOrThrow();
        final List<StatByCategory> statByCategories = data.stat.statByCategories;

        step("Verify number of categories matches", () ->
                Assertions.assertEquals(spendingsTestData.size(), statByCategories.size())
        );

        Map<String, Double> expectedAmounts = Map.of(
                "Машина", 89900.0,
                "Бег", 6666.67,
                "Учеба", 14400.0,
                "Отдых", 42.0
        );

        for (SpendJson spend : spendingsTestData) {
            StatByCategory categoryStat = statByCategories.stream()
                    .filter(s -> s.categoryName.equals(spend.category().name()))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("Category not found: " + spend.category().name()));

            step("Verify data for category: " + spend.category().name(), () -> {
                Assertions.assertEquals(spend.category().name(), categoryStat.categoryName);
                Assertions.assertEquals(expectedAmounts.get(spend.category().name()), categoryStat.sum, 0.01);
                Assertions.assertEquals(guru.qa.type.CurrencyValues.RUB.rawValue, categoryStat.currency.rawValue);
            });
        }
    }

    @Test
    @User(
            categories = {@Category(archived = true)}
    )
    @ApiLogin
    @DisplayName("GQL: Category should be archived")
    void categoryStatShouldBeArchivedFromGateway(@Token String token, UserJson user) {
        final CategoryJson categoryJson = user.testData().categories().getFirst();
        ApolloCall<CategoriesQuery.Data> statCall = apolloClient.query(new CategoriesQuery()).addHttpHeader("authorization", token);
        ApolloResponse<CategoriesQuery.Data> apolloResponse = Rx2Apollo.single(statCall).blockingGet();

        final CategoriesQuery.Data data = apolloResponse.dataOrThrow();
        final CategoriesQuery.Category category = data.user.categories.getFirst();

        step("Verify that category name in equals spending category name", () ->
                Assertions.assertEquals(categoryJson.name(), category.name)
        );
        step("Verify that category is archived", () ->
                Assertions.assertTrue(category.archived)
        );
    }

    @Test
    @User(
            categories = {@Category()}
    )
    @ApiLogin
    @DisplayName("GQL: Category should not be archived")
    void categoryStatShouldBeActiveFromGateway(@Token String token, UserJson user) {
        final CategoryJson categoryJson = user.testData().categories().getFirst();
        ApolloCall<CategoriesQuery.Data> statCall = apolloClient.query(new CategoriesQuery()).addHttpHeader("authorization", token);
        ApolloResponse<CategoriesQuery.Data> apolloResponse = Rx2Apollo.single(statCall).blockingGet();

        final CategoriesQuery.Data data = apolloResponse.dataOrThrow();
        final CategoriesQuery.Category category = data.user.categories.getFirst();

        step("Verify that category name in equals spending category name", () ->
                Assertions.assertEquals(categoryJson.name(), category.name)
        );
        step("Verify that category is not archived", () ->
                Assertions.assertFalse(category.archived)
        );
    }
}
