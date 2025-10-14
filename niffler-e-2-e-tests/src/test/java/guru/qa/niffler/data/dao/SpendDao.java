package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.util.UUID;

public interface SpendDao {
    SpendEntity createSpend(SpendEntity spend);
}
