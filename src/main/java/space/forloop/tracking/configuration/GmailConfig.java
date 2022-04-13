package space.forloop.tracking.configuration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.forloop.tracking.properties.AppProperties;
import space.forloop.tracking.services.GmailServiceImpl;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GmailConfig {

  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

  private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);

  private final AppProperties appProperties;

  @Bean
  public Gmail client() throws GeneralSecurityException, IOException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

    return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(appProperties.applicationName())
        .build();
  }

  private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

    final GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, getGoogleClientSecrets(), SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new File(appProperties.tokenDirectory())))
            .setAccessType("offline")
            .build();

    final LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  private GoogleClientSecrets getGoogleClientSecrets() throws IOException {

    final InputStream inputStream =
        GmailServiceImpl.class.getResourceAsStream(appProperties.credentials());

    if (inputStream == null) {
      throw new FileNotFoundException("Resource not found: " + appProperties.credentials());
    }

    return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
  }
}
