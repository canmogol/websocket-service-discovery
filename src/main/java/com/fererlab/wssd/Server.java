package com.fererlab.wssd;

import com.fererlab.wssd.rest.PublishSubscribeResource;
import com.fererlab.wssd.rest.WssdApplication;
import io.undertow.Handlers;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Server {

  public static final String STATIC_CONTENT_PATH = "STATIC_CONTENT_PATH";

  private static final Logger logger = Logger.getLogger(Server.class.getName());

  static {
    final String loggingProperties = "logging.properties";
    try {
      LogManager.getLogManager().readConfiguration(Server.class.getClassLoader().getResourceAsStream(loggingProperties));
    } catch (IOException e) {
      logger.severe(String.format("could not load logging file: %1s", loggingProperties));
    }
  }

  public static void main(final String[] args) {

    UndertowJaxrsServer server = new UndertowJaxrsServer();

    ResteasyDeployment resteasyDeployment = new ResteasyDeployment();
    resteasyDeployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory");
    resteasyDeployment.setApplicationClass(WssdApplication.class.getName());

    WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
    webSocketDeploymentInfo.addEndpoint(PublishSubscribeResource.class);

    DeploymentInfo deploymentInfo = server.undertowDeployment(resteasyDeployment, "/");
    deploymentInfo.setClassLoader(Server.class.getClassLoader());
    deploymentInfo.setContextPath("/api");
    deploymentInfo.setDeploymentName("cdi deployment name cannot be null");
    deploymentInfo.addListeners(Servlets.listener(org.jboss.weld.environment.servlet.Listener.class));
    deploymentInfo.addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME, webSocketDeploymentInfo);

    server.deploy(deploymentInfo);

    server.addResourcePrefixPath(
      "/",
      Handlers.resource(
        new PathResourceManager(
          Paths.get(System.getProperty("user.home")),
          100
        )
      ).setDirectoryListingEnabled(true)
    );

    server.start();

  }


}
