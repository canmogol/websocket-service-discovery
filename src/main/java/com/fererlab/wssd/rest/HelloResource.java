package com.fererlab.wssd.rest;

import com.fererlab.wssd.service.HelloService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Random;

@Path("/hi")
public class HelloResource {

  @Inject
  HelloService service;

  double random = new Random().nextDouble();

  @GET
  @Produces("text/plain")
  public String sayHi(@QueryParam("name") String name) {
    return service.sayHi(name) + " REST:" + random;
  }

}
