package space.forloop.tracking.listeners;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import space.forloop.tracking.services.GmailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartListener {

  private final GmailService gmailService;

  @SneakyThrows
  @EventListener(ApplicationReadyEvent.class)
  public void applicationReady() {
    gmailService.getEmails();
  }
}
