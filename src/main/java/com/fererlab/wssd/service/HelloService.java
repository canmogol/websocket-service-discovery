package com.fererlab.wssd.service;


import com.fererlab.wssd.property.Property;

import javax.inject.Inject;
import java.util.Random;

public class HelloService {

  @Inject
  @Property("application.name")
  private String applicationName;

  private double random = new Random().nextDouble();

  public String sayHi(String name) {
    return String.format("App name '%1s' Hi %2s SERVICE: %3s", applicationName, name, random);
  }

}
