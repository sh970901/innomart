package com.hun.market;

import com.hun.market.core.web.MarketRunApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableRetry
@EnableScheduling
public class MarketApplication {

	public static void main(String[] args) {
		MarketRunApplication.run(MarketApplication.class, args);
	}
}
