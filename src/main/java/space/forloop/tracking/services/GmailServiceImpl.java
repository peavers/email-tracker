package space.forloop.tracking.services;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailServiceImpl implements GmailService {

  private static final String USER_ID = "me";

  private final Gmail client;

  @Override
  public void getEmails() throws IOException {
    final ListMessagesResponse messagesResponse = client.users().messages().list(USER_ID).execute();
    final List<Message> messages = messagesResponse.getMessages();
    final List<String> content = new LinkedList<>();

    for (final Message message : messages) {
      content.add(getContent(client.users().messages().get(USER_ID, message.getId()).execute()));
    }

    log.info("Content size: {}", content.size());
  }

  private String getContent(final Message message) {
    final StringBuilder stringBuilder = new StringBuilder();
    getPlainTextFromMessageParts(message.getPayload().getParts(), stringBuilder);

    final byte[] bodyBytes = Base64.decodeBase64(stringBuilder.toString());

    return new String(bodyBytes, StandardCharsets.UTF_8);
  }

  private void getPlainTextFromMessageParts(
      final List<MessagePart> messageParts, final StringBuilder stringBuilder) {
    if (messageParts == null) {
      return;
    }

    for (final MessagePart messagePart : messageParts) {
      if (messagePart.getMimeType().equals(MediaType.TEXT_PLAIN_VALUE)) {
        stringBuilder.append(messagePart.getBody().getData());
      }

      if (messagePart.getParts() != null) {
        getPlainTextFromMessageParts(messagePart.getParts(), stringBuilder);
      }
    }
  }
}
