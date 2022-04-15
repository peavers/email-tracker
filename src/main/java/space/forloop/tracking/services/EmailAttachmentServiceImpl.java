package space.forloop.tracking.services;

import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAttachmentServiceImpl implements EmailAttachmentService {

  private final GmailService gmailService;

  @Override
  public Map<String, List<Path>> downloadAttachments(final String messageId) throws Exception {
    final Map<String, List<Path>> downloads = new HashMap<>();

    final List<MessagePart> parts = gmailService
      .getMessage(messageId).orElseThrow(() -> new Exception("No message found"))
      .getPayload()
      .getParts();

    if (parts == null) {
      return downloads;
    }

    parts
      .parallelStream()
      .filter(messagePart -> StringUtils.isNotBlank(messagePart.getFilename()))
      .map(messagePart -> downloadFile(messageId, messagePart))
      .forEach(path -> addToResults(messageId, downloads, path));

    return downloads;
  }

  private void addToResults(final String messageId, final Map<String, List<Path>> downloads, final Path path) {
    final List<Path> paths = downloads.getOrDefault(messageId, new ArrayList<>());
    paths.add(path);

    downloads.put(messageId, paths);
  }

  @SneakyThrows
  private Path downloadFile(final String messageId, final MessagePart part) {
    final String attachmentId = part.getBody().getAttachmentId();
    final MessagePartBody attachment = gmailService.getAttachment(messageId, attachmentId);

    final Path directory = Files.createDirectories(Paths.get(".files", messageId));
    final Path file = new File(directory + "/" + part.getFilename()).toPath();

    Files.write(file.toAbsolutePath(), Base64.decodeBase64(attachment.getData()));

    return file;
  }
}
