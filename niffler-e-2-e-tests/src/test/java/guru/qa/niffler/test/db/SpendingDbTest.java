package guru.qa.niffler.test.db;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.spend.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.impl.spend.SpendRepositoryJdbc;
import guru.qa.niffler.jupiter.extension.NonStaticBrowsersExtension;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(NonStaticBrowsersExtension.class)
public class SpendingDbTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void findSpendingByIdTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        Optional<SpendJson> byId = spendDbClient.findById(
                UUID.fromString("1328a312-b5bc-11f0-a017-aa5c32f82d84"));
    }

    @Test
    void findSpendByNameAndDescription() {
        SpendDbClient spendDbClient = new SpendDbClient();
        Optional<SpendJson> duck = spendDbClient.findByUsernameAndSpendDescription("duck",
                "11111");
    }

    @Test
    void createSpendingTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("1111111222qaa");
        categoryEntity.setUsername("duck");
        categoryEntity.setArchived(false);

        SpendEntity entity = new SpendEntity();
        entity.setUsername("duck");
        entity.setCurrency(CurrencyValues.RUB);
        entity.setSpendDate(new Date());
        entity.setAmount(123.0);
        entity.setDescription("1111111222qaa");
        entity.setCategory(categoryEntity);
        SpendJson spendJson = SpendJson.fromEntity(entity);
        spendDbClient.create(spendJson);
    }

    @Test
    void deleteSpendingTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("555qaa");
        categoryEntity.setUsername("duck");
        categoryEntity.setArchived(false);

        SpendEntity entity = new SpendEntity();
        entity.setUsername("duck");
        entity.setCurrency(CurrencyValues.RUB);
        entity.setSpendDate(new Date());
        entity.setAmount(123.0);
        entity.setDescription("55qa1zs1dfg");
        entity.setCategory(categoryEntity);

        SpendJson spendJson = SpendJson.fromEntity(entity);

        spendDbClient.create(spendJson);
        spendDbClient.remove(spendJson);
    }

    @Test
    void updateSpendingTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("777qaa");
        categoryEntity.setUsername("duck");
        categoryEntity.setArchived(false);

        SpendEntity entity = new SpendEntity();
        entity.setUsername("duck");
        entity.setCurrency(CurrencyValues.RUB);
        entity.setSpendDate(new Date());
        entity.setAmount(123.0);
        entity.setDescription("777qazsdfg");
        entity.setCategory(categoryEntity);
        SpendJson spendJson = SpendJson.fromEntity(entity);

        spendDbClient.create(spendJson);
        entity.setAmount(222.0);
        spendDbClient.update(spendJson);
    }

    @Test
    void createSpendTest() {
        SpendRepositoryJdbc spendRepositoryJdbc = new SpendRepositoryJdbc();
        CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("1234qaa");
        categoryEntity.setUsername("duck");
        categoryEntity.setArchived(false);
        CategoryEntity savedCategory = categoryDaoJdbc.create(categoryEntity);

        SpendEntity entity = new SpendEntity();
        entity.setUsername("duck");
        entity.setCurrency(CurrencyValues.RUB);
        entity.setSpendDate(new Date());
        entity.setAmount(123.0);
        entity.setDescription("1234qazsdfg");
        entity.setCategory(savedCategory);

        spendRepositoryJdbc.create(entity);
    }

    @Test
    void removeSpendTest() {
        SpendRepositoryJdbc spendRepositoryJdbc = new SpendRepositoryJdbc();
        CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("7819");
        categoryEntity.setUsername("duck");
        categoryEntity.setArchived(false);
        CategoryEntity savedCategory = categoryDaoJdbc.create(categoryEntity);

        SpendEntity entity = new SpendEntity();
        entity.setUsername("duck");
        entity.setCurrency(CurrencyValues.RUB);
        entity.setSpendDate(new Date());
        entity.setAmount(123.0);
        entity.setDescription("7819qazsdfg");
        entity.setCategory(savedCategory);

        SpendEntity savedSpend = spendRepositoryJdbc.create(entity);
        spendRepositoryJdbc.remove(savedSpend);
    }
}

