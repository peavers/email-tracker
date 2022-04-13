package space.forloop.tracking.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface GmailService {

  Map<String, String> getContent() throws GeneralSecurityException, IOException;
}
