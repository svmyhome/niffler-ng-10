package guru.qa.niffler.data.jpa;

import guru.qa.niffler.data.tpl.DataSources;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;

public class EntityManagers {

  private static final Map<String, EntityManagerFactory> emfs = new ConcurrentHashMap<>();

  private EntityManagers() {
  }

  public @Nonnull
  static EntityManager em(@Nonnull String jdbcUrl) {
    return new ThreadSafeEntityManager(emfs.computeIfAbsent(
        jdbcUrl,
        key -> {
          DataSources.dataSource(jdbcUrl);
          return Persistence.createEntityManagerFactory(jdbcUrl);
        }
    ).createEntityManager());
  }
}
