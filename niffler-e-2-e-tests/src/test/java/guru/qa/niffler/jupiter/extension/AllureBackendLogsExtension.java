package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AllureBackendLogsExtension implements SuiteExtension {

  public static final String caseName = "Niffler back logs";
  private static final List<LogFileConfig> logFileConfigs = Arrays.asList(
      new LogFileConfig("Niffler-auth logs", "./logs/niffler-auth/app.log"),
      new LogFileConfig("Niffler-userdata logs", "./logs/niffler-userdata/userdata.log"),
      new LogFileConfig("Niffler-spend logs", "./logs/niffler-spend/spend.log"),
      new LogFileConfig("Niffler-gateway logs", "./logs/niffler-gateway/gateway.log"),
      new LogFileConfig("Niffler-currency logs", "./logs/niffler-currency/currency.log")
  );

  @Override
  public void afterSuite() {
    final AllureLifecycle allureLifecicle = Allure.getLifecycle();
    final String caseId = UUID.randomUUID().toString();

    allureLifecicle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
    allureLifecicle.startTestCase(caseId);

    try {
      attachLogFiles(allureLifecicle);
//      allureLifecicle.addAttachment(
//          "Niffler-auth logs",
//          "text/html",
//          ".log",
//          Files.newInputStream(Path.of("./logs/niffler-auth/app.log"))
//      );
//      allureLifecicle.addAttachment(
//          "Niffler-userdata logs",
//          "text/html",
//          ".log",
//          Files.newInputStream(Path.of("./logs/niffler-userdata/userdata.log"))
//      );
//      allureLifecicle.addAttachment(
//          "Niffler-spend logs",
//          "text/html",
//          ".log",
//          Files.newInputStream(Path.of("./logs/niffler-spend/spend.log"))
//      );
//      allureLifecicle.addAttachment(
//          "Niffler-gateway logs",
//          "text/html",
//          ".log",
//          Files.newInputStream(Path.of("./logs/niffler-gateway/gateway.log"))
//      );
//      allureLifecicle.addAttachment(
//          "Niffler-currency logs",
//          "text/html",
//          ".log",
//          Files.newInputStream(Path.of("./logs/niffler-currency/currency.log"))
//      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    allureLifecicle.stopTestCase(caseId);
    allureLifecicle.writeTestCase(caseId);
  }

  private void attachLogFiles(AllureLifecycle allureLifecycle) throws IOException {
    for (LogFileConfig config : logFileConfigs) {
      allureLifecycle.addAttachment(
          config.displayName,
          "text/html",
          ".log",
          Files.newInputStream(Path.of(config.filePath))
      );
    }
  }

  private static class LogFileConfig {

    final String displayName;
    final String filePath;

    LogFileConfig(String displayName, String filePath) {
      this.displayName = displayName;
      this.filePath = filePath;
    }
  }
}
