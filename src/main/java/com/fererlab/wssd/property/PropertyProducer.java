package com.fererlab.wssd.property;


import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class PropertyProducer {

  public static final String INTERNAL_APPLICATION_PROPERTIES = "/application.properties";
  private final Properties properties = new Properties();
  private final static Logger logger = Logger.getLogger(PropertyProducer.class.getName());

  @Property
  @Produces
  public String produceString(final InjectionPoint ip) {
    return this.properties.getProperty(getKey(ip));
  }

  @Property
  @Produces
  public int produceInt(final InjectionPoint ip) {
    return Integer.valueOf(this.properties.getProperty(getKey(ip)));
  }

  @Property
  @Produces
  public boolean produceBoolean(final InjectionPoint ip) {
    return Boolean.valueOf(this.properties.getProperty(getKey(ip)));
  }

  private String getKey(final InjectionPoint ip) {
    if (ip.getAnnotated().isAnnotationPresent(Property.class)
      && !ip.getAnnotated().getAnnotation(Property.class).value().isEmpty()) {
      return ip.getAnnotated().getAnnotation(Property.class).value();
    } else {
      return ip.getMember().getName();
    }
  }

  @PostConstruct
  public void init() {
    try {
      InputStream inputStream;
      final String wssdProperty = System.getProperty("wssd.properties");
      if (wssdProperty != null) {
        final Path wssdPropertiesFilePath = Paths.get(wssdProperty);
        inputStream = Files.newInputStream(wssdPropertiesFilePath, StandardOpenOption.READ);
      } else {
        logger.log(Level.INFO, "no system property defined with key 'wssd.properties' will use internal " + INTERNAL_APPLICATION_PROPERTIES);
        inputStream = PropertyProducer.class.getResourceAsStream(INTERNAL_APPLICATION_PROPERTIES);
        if (inputStream == null) {
          throw new RuntimeException("No properties file found");
        }
      }

      this.properties.load(inputStream);
    } catch (final Exception e) {
      throw new RuntimeException("Configuration file could not be loaded", e);
    }
  }

}