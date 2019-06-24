package net.pdp7.shared_rankings.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import net.pdp7.shared_rankings.calc.Ranker;

public interface Ranking {
	public String getName();

	public Collection<String> getParticipants();

	public void addParticipant(String newParticipant, ParticipantRanking participantRanking);

	public ParticipantRanking getParticipantRanking(String participant);

	public default List<String> getRanked() {
		List<List<List<String>>> votes = getParticipants().stream()
				.map(participant -> getParticipantRanking(participant).getElements()).collect(Collectors.toList());
		return new Ranker().rank(votes);
	}

	public void addEmitter(SseEmitter emitter);

	public void update();

	public Set<String> getAllVoted();

	public void renameElement(String element, String newName);

	public void deleteElement(String element);
}
