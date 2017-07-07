package io.pivotal.pal.tracker.timesheets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestOperations;

import java.util.TimeZone;


@EnableWebSecurity
@EnableResourceServer
@EnableEurekaClient
@EnableOAuth2Client
@SpringBootApplication
@ComponentScan({"io.pivotal.pal.tracker.timesheets", "io.pivotal.pal.tracker.restsupport"})
public class App {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(App.class, args);
    }

    @Bean
    ProjectClient projectClient(
        RestOperations restOperations,
        @Value("${registration.server.endpoint}") String registrationEndpoint
    ) {
        return new ProjectClient(restOperations, registrationEndpoint);
    }
}
