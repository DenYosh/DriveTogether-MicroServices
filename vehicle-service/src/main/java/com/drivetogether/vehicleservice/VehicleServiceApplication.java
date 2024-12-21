package com.drivetogether.vehicleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class VehicleServiceApplication {

    @Autowired
    public void printEnvironment(Environment environment) {
        System.out.println("### Active Profiles ###");
        for (String profile : environment.getActiveProfiles()) {
            System.out.println("Active Profile: " + profile);
        }

        System.out.println("### System Properties ###");
        System.getProperties().forEach((key, value) -> System.out.println(key + " = " + value));

        System.out.println("### Environment Variables ###");
        System.getenv().forEach((key, value) -> System.out.println(key + " = " + value));
    }

    public static void main(String[] args) {
        SpringApplication.run(VehicleServiceApplication.class, args);
    }

}
