package com.hdamarcelo.application;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.reflections.Reflections;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

/**
 * API and Swagger setup
 */
@ApplicationPath("/api")
@ApplicationScoped
public class JakartaApplication extends Application {

    private Set<Class<?>> classes;

    @PostConstruct
    void init() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("0.0.1");
        beanConfig.setTitle("movie-db-rest-example");
        beanConfig.setBasePath("/movie-db-rest-example/api/");
        beanConfig.setResourcePackage("com.hdamarcelo.application");
        beanConfig.setScan(true);

        classes = new HashSet<>();

        classes.add(ApiListingResource.class);
        classes.add(SwaggerSerializers.class);

        Reflections reflections = new Reflections("com.hdamarcelo");

        classes.addAll(reflections.getTypesAnnotatedWith(Path.class, true));
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
