package space.forloop.tracking.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import space.forloop.tracking.services.GmailService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartListener {

  private final GmailService gmailService;

  @EventListener(ApplicationReadyEvent.class)
  public void applicationReady() {
    try {
      // TODO: Find something useful to do with this...
      final Map<String, String> content = gmailService.getContent();
    } catch (final GeneralSecurityException | IOException e) {
      log.error("Error fetching emails: {}", e.getMessage(), e);
    }
  }
}
