package space.forloop.tracking.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import space.forloop.tracking.services.EmailContentService;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartListener {

  private final EmailContentService emailContentService;

  @EventListener(ApplicationReadyEvent.class)
  public void applicationReady() {
    final Map<String, String> content = emailContentService.getContent();

    content.values().forEach(log::info);
  }
}
