package space.forloop.tracking.services;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface GmailService {

  Optional<Message> getMessage(String messageId);

  List<Message> getMessages();

  MessagePartBody getAttachment(String messageId, String attachmentId) throws IOException;
}
