package com.blueground.appserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info=@Info(title="Assignment API Documentation"))
public class AppserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppserverApplication.class, args);
    }

}
