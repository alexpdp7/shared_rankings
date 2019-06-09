package net.pdp7.shared_rankings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import net.pdp7.shared_rankings.mvc.MainController;
import net.pdp7.shared_rankings.service.RankingService;
import net.pdp7.shared_rankings.service.simple.SimpleRankingServiceImpl;

@SpringBootApplication
public class SharedRankings {

	@Bean
	RankingService rankingService() {
		return new SimpleRankingServiceImpl(true);
	}

	MainController homeController() {
		return new MainController(rankingService());
	}

	public static void main(String[] args) {
		SpringApplication.run(SharedRankings.class, args);
	}

}
