package edu.tomerbu.lec4tdd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(/*exclude = SecurityAutoConfiguration.class*/)
public class Lec4TddApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lec4TddApplication.class, args);
    }

}
