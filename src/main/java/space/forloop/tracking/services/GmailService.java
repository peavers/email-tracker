package space.forloop.tracking.services;

import com.google.api.services.gmail.model.Message;

import java.util.List;
import java.util.Optional;

public interface GmailService {

  Optional<Message> getMessage(final String messageId);

  List<Message> getMessages();
}
