package com.hun.market.core.web;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class MarketRunApplication {
    public MarketRunApplication() {
    }
    public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return SpringApplication.run(primarySource, args);
    }

    public static void init() throws IOException {
        log.info("Go  !!!!!!!");
    }
}
