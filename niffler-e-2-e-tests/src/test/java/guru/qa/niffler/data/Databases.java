package guru.qa.niffler.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class Databases {

  private static final Map<String, DataSource> datasources = new ConcurrentHashMap<>();
  private static final Map<Long, Map<String, Connection>> threadConnections = new ConcurrentHashMap();

  private Databases() {
  }

  public static <T> T transaction(Function<Connection, T> function, String jdbcUrl) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setAutoCommit(false);
      T result = function.apply(connection);
      connection.commit();
      connection.setAutoCommit(true);
      return result;
    } catch (SQLException e) {
      if (connection != null) {
        try {
          connection.rollback();
          connection.setAutoCommit(true);
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      throw new RuntimeException(e);
    }
  }

  public static void transaction(Consumer<Connection> consumer, String jdbcUrl) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setAutoCommit(false);
      consumer.accept(connection);
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      if (connection != null) {
        try {
          connection.rollback();
          connection.setAutoCommit(true);
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      throw new RuntimeException(e);
    }
  }


  private static DataSource dataSource(String jdbcUrl) {
    return datasources.computeIfAbsent(
        jdbcUrl,
        key -> {
          PGSimpleDataSource ds = new PGSimpleDataSource();
          ds.setURL(jdbcUrl);
          ds.setUser("postgres");
          ds.setPassword("secret");
          return ds;
        }
    );
  }

  private static Connection connection(String jdbcUrl) throws SQLException {
    return threadConnections.computeIfAbsent(
        Thread.currentThread().threadId(),
        key -> {
          try {
            return new HashMap<>(Map.of(
                jdbcUrl,
                dataSource(jdbcUrl).getConnection()
            ));
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
    ).computeIfAbsent(
        jdbcUrl,
        key -> {
          try {
            return dataSource(jdbcUrl).getConnection();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
    );
  }

  public static void closeAllConnections() {
    for (Map<String, Connection> connectionMap : threadConnections.values()) {
      for (Connection connection : connectionMap.values()) {
        try {
          if (connection != null & connection.isClosed()) {
            connection.close();
          }
        } catch (SQLException e) {
          //NOP
        }
      }
    }
  }
}
