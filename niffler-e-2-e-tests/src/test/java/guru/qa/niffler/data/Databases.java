package guru.qa.niffler.data;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;

public class Databases {

  private static final Map<String, DataSource> datasources = new ConcurrentHashMap<>();
  private static final Map<Long, Map<String, Connection>> threadConnections = new ConcurrentHashMap();

  private Databases() {
  }

  public static <T> T transaction(Function<Connection, T> function, String jdbcUrl) {
    return transaction(function, jdbcUrl, Connection.TRANSACTION_REPEATABLE_READ);
  }

  public static <T> T transaction(Function<Connection, T> function, String jdbcUrl,
      int isolationLvl) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setTransactionIsolation(isolationLvl);
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
    transaction(consumer, jdbcUrl, Connection.TRANSACTION_REPEATABLE_READ);
  }

  public static void transaction(Consumer<Connection> consumer, String jdbcUrl, int isolationLvl) {
    Connection connection = null;
    try {
      connection = connection(jdbcUrl);
      connection.setTransactionIsolation(isolationLvl);
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

  public static <T> T xaTransaction(XaFunction<T>... actions) {
    return xaTransaction(Connection.TRANSACTION_REPEATABLE_READ, actions);
  }

  public static <T> T xaTransaction(int isolationLvl, XaFunction<T>... actions) {
    UserTransaction ut = new UserTransactionImp();
    try {
      ut.begin();
      T result = null;
      for (XaFunction<T> action : actions) {
        Connection conn = getNewConnection(action.jdbcUrl);
        conn.setTransactionIsolation(isolationLvl);
        result = action.function.apply(conn);
      }

      ut.commit();
      return result;
    } catch (Exception e) {
      try {
        ut.rollback();
      } catch (SystemException ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException(e);
    }
  }

  public static void xaTransaction(XaConsumer... actions) {
    xaTransaction(Connection.TRANSACTION_REPEATABLE_READ, actions);
  }

  public static void xaTransaction(int isolationLvl, XaConsumer... actions) {
    UserTransaction ut = new UserTransactionImp();
    try {
      ut.begin();
      for (XaConsumer action : actions) {
        Connection conn = getNewConnection(action.jdbcUrl);
        conn.setTransactionIsolation(isolationLvl);
        action.consumer.accept(conn);
      }
      ut.commit();
    } catch (Exception e) {
      try {
        ut.rollback();
      } catch (SystemException ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException(e);
    }
  }

  private static DataSource dataSource(String jdbcUrl) {
    return datasources.computeIfAbsent(
        jdbcUrl,
        key -> {
          AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
          final String uniqId = StringUtils.substringAfter(jdbcUrl, "5432/");
          dsBean.setUniqueResourceName(uniqId);
          dsBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
          Properties props = new Properties();
          props.put("URL", jdbcUrl);
          props.put("user", "postgres");
          props.put("password", "secret");
          dsBean.setXaProperties(props);
          dsBean.setPoolSize(10);
          return dsBean;
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
          if (connection != null && !connection.isClosed()) {
            connection.close();
          }
        } catch (SQLException e) {
          //NOP
        }
      }
    }
  }

  private static Connection getNewConnection(String jdbcUrl) throws SQLException {
    return dataSource(jdbcUrl).getConnection();
  }

  public record XaFunction<T>(Function<Connection, T> function, String jdbcUrl) {

  }

  public record XaConsumer(Consumer<Connection> consumer, String jdbcUrl) {

  }
}
