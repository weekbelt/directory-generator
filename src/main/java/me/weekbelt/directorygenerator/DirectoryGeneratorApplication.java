package me.weekbelt.directorygenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DirectoryGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DirectoryGeneratorApplication.class, args);
    }

}
