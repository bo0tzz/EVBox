package me.bo0tzz.evbox.configuration;

import org.springframework.context.annotation.Bean;

import java.security.SecureRandom;
import java.util.Random;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public Random randomBean() {
        return new SecureRandom();
    }

}
