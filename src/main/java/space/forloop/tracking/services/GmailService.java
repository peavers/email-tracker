package space.forloop.tracking.services;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GmailService {

  void getEmails() throws GeneralSecurityException, IOException;
}
