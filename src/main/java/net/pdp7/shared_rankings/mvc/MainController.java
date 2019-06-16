package net.pdp7.shared_rankings.mvc;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import net.pdp7.shared_rankings.service.ParticipantRanking;
import net.pdp7.shared_rankings.service.Ranking;
import net.pdp7.shared_rankings.service.RankingService;
import net.pdp7.shared_rankings.service.simple.SimpleParticipantRankingImpl;

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
		Ranking ranking_ = rankingService.getRanking(ranking);
		ranking_.addParticipant(newParticipant, new SimpleParticipantRankingImpl(ranking_));
		return "redirect:/ranking/" + ranking;
	}

	@GetMapping("/ranking/{ranking}")
	public ModelAndView ranking(@PathVariable String ranking) {
		return new ModelAndView("ranking", Map.of("ranking", rankingService.getRanking(ranking)));
	}

	@GetMapping("/ranking/{ranking}/stream")
	public SseEmitter rankingStream(@PathVariable String ranking) throws IOException {
		Ranking ranking_ = rankingService.getRanking(ranking);
		SseEmitter emitter = new SseEmitter();
		ranking_.addEmitter(emitter);
		return emitter;
	}

	@GetMapping("/ranking/{ranking}/participant/{participant}")
	public ModelAndView ranking(@PathVariable String ranking, @PathVariable String participant) {
		return new ModelAndView("ranking_participant", Map.of("ranking", rankingService.getRanking(ranking)));
	}

	@GetMapping("/ranking/{ranking}/participant/{participant}/stream")
	public SseEmitter rankingStream(@PathVariable String ranking, @PathVariable String participant) throws IOException {
		SseEmitter sseEmitter = new SseEmitter();
		ParticipantRanking participantRanking = rankingService.getRanking(ranking).getParticipantRanking(participant);
		participantRanking.addEmitter(sseEmitter);
		return sseEmitter;
	}

	@PostMapping("/ranking/{ranking}/participant/{participant}/update")
	public HttpHeaders rankingUpdate(@PathVariable String ranking, @PathVariable String participant,
			@RequestBody List<List<String>> newRanking) throws IOException {
		rankingService.getRanking(ranking).getParticipantRanking(participant).setElements(newRanking);
		return new HttpHeaders();
	}

}
