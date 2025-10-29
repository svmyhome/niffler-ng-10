package guru.qa.niffler.data.tpl;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Connections {

    private final static Map<String, JdbcConnectionHolder> holders = new ConcurrentHashMap<>();

    private Connections() {
    }

    public JdbcConnectionHolder holder(String jdbcUrl) {
        return holders.computeIfAbsent(
                jdbcUrl,
                key -> new JdbcConnectionHolder(DataSources.dataSource(jdbcUrl))
        );
    }

    public JdbcConnectionHolders holders(String... jdbcUrl) {
        List<JdbcConnectionHolder> result = new ArrayList<>();
        for (String url: jdbcUrl) {
           result.add(holder(url));
        }
        return new JdbcConnectionHolders(result);
    }

    public static void closeAllConnections() {
        holders.values().forEach(JdbcConnectionHolder::closeAllConnections);
    }
}
