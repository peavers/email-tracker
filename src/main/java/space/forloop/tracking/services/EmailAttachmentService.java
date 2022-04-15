package space.forloop.tracking.services;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface EmailAttachmentService {

  Map<String, List<Path>> downloadAttachments(final String messageId) throws Exception;

}
