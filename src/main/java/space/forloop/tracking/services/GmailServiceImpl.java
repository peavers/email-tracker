package space.forloop.tracking.services;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailServiceImpl implements GmailService {

  private static final String USER_ID = "me";

  private final Gmail client;

  @Override
  public Optional<Message> getMessage(final String messageId) {
    try {
      return Optional.of(client.users().messages().get(USER_ID, messageId).execute());
    } catch (final IOException e) {
      log.error("Error getting message {}", e.getMessage(), e);
    }

    return Optional.empty();
  }

  @Override
  public List<Message> getMessages() {
    try {
      return client.users().messages().list(USER_ID).execute().getMessages();
    } catch (final IOException e) {
      log.error("Error getting messages {}", e.getMessage(), e);
    }

    return new ArrayList<>();
  }

  @Override
  public MessagePartBody getAttachment(final String messageId, final String attachmentId) throws IOException {
    return client.users().messages().attachments().get(USER_ID, messageId, attachmentId).execute();
  }
}
