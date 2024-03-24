package com.hse.kpo.hw.restaraunt_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {

    @Value("${orders.oneTimeMax}")
    private int NUMBER_OF_THREADS;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }
}
