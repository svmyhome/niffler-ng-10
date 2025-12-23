package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return "docker".equals(System.getProperty("test.env"))
        ? DockerConfig.INSTANCE
        : LocalConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();

  String spendJdbcUrl();

  String ghUrl();

  String authUrl();

  String authJdbcUrl();

  String userdataUrl();

  String userdataJdbcUrl();

  String currencyUrl();

  String currencyJdbcUrl();

  String gwUrl();
}
