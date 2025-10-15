package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.impl.SpendDaoJdbc;
import guru.qa.niffler.model.SpendJson;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = categoryDao.createCategory(
                    spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
        }
        return SpendJson.fromEntity(
                spendDao.createSpend(spendEntity)
        );
    }
//
//    public Optional<SpendJson> findSpendById(UUID id) {
//        return Optional.of(SpendJson.fromEntity(spendDao.findSpendById(id)));
//    }

}
