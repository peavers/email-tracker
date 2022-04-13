package space.forloop.tracking.services;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailContentServiceImpl implements EmailContentService {

  private final GmailService gmailService;

  @Override
  public Map<String, String> getContent() {

    return gmailService.getMessages()
      .parallelStream()
      .map(message -> gmailService.getMessage(message.getId()))
      .flatMap(Optional::stream)
      .filter(message -> Objects.nonNull(message.getPayload().getParts()))
      .collect(Collectors.toMap(Message::getId, this::getContent));
  }

  private String getContent(final Message message) {
    final StringBuilder stringBuilder = new StringBuilder();
    final List<MessagePart> messageParts = message.getPayload().getParts().stream().filter(Objects::nonNull).toList();

    extractText(messageParts, stringBuilder);

    return new String(Base64.decodeBase64(stringBuilder.toString()), StandardCharsets.UTF_8);
  }

  private void extractText(final List<MessagePart> messageParts, final StringBuilder stringBuilder) {
    for (final MessagePart messagePart : messageParts) {
      if (messagePart.getMimeType().equals(MediaType.TEXT_PLAIN_VALUE)) {
        stringBuilder.append(messagePart.getBody().getData());
      }

      if (messagePart.getParts() != null) {
        extractText(messagePart.getParts(), stringBuilder);
      }
    }
  }

}
