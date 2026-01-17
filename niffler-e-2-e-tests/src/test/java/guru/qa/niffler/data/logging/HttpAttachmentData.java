package guru.qa.niffler.data.logging;

import io.qameta.allure.attachment.AttachmentData;
import lombok.Getter;

@Getter
public class HttpAttachmentData implements AttachmentData {

  private final String name;
  private final String body;

  public HttpAttachmentData(String name, String body) {
    this.name = name;
    this.body = body;
  }
}
