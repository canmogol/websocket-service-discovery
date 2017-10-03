package com.fererlab.wssd.rest;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class WssdApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> classes = new HashSet<>();
        classes.add(HelloResource.class);
        return classes;
    }

}
