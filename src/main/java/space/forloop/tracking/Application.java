package space.forloop.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import space.forloop.tracking.properties.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
public class Application {

  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
