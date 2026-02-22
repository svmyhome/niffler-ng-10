package guru.qa.niffler.test.gql;

import static io.qameta.allure.Allure.step;
import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.CategoriesQuery;
import guru.qa.StatQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.user.UserJson;
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
