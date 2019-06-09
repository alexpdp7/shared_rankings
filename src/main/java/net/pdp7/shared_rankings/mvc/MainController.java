package net.pdp7.shared_rankings.mvc;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.pdp7.shared_rankings.service.RankingService;

@Controller
public class MainController {

	public final RankingService rankingService;

	public MainController(RankingService rankingService) {
		this.rankingService = rankingService;
	}

	@GetMapping("/")
	public ModelAndView home() {
		Collection<String> rankings = rankingService.getRankings();
		return new ModelAndView("index", Map.of("rankings", rankings));
	}

	@PostMapping("/addRanking")
	public String addRanking(@RequestParam String newRanking) {
		rankingService.addRanking(newRanking);
		return "redirect:/";
	}

	@PostMapping("/addParticipant")
	public String addParticipant(@RequestParam String ranking, @RequestParam String newParticipant) {
		rankingService.getRanking(ranking).addParticipant(newParticipant);
		return "redirect:/ranking/" + ranking;
	}

	
	@GetMapping("/ranking/{ranking}")
	public ModelAndView ranking(@PathVariable String ranking) {
		return new ModelAndView("ranking", Map.of("ranking", rankingService.getRanking(ranking)));
	}
}
