package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();

  String spendJdbcUrl();

  String ghUrl();

  String authUrl();

  String userdataUrl();

  String currencyUrl();

  String gwUrl();
}
