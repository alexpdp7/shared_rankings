package net.pdp7.shared_rankings.service.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import net.pdp7.shared_rankings.service.ParticipantRanking;
import net.pdp7.shared_rankings.service.Ranking;

public class SimpleRankingImpl implements Ranking {

	public final String name;
	public final Map<String, ParticipantRanking> participantRankings = new HashMap<String, ParticipantRanking>();
	public final List<SseEmitter> emitters = new ArrayList<SseEmitter>();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public SimpleRankingImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Collection<String> getParticipants() {
		return participantRankings.keySet();
	}

	@Override
	public void addParticipant(String newParticipant, ParticipantRanking participantRanking) {
		participantRankings.put(newParticipant, participantRanking);
	}

	@Override
	public ParticipantRanking getParticipantRanking(String participant) {
		return participantRankings.get(participant);
	}

	@Override
	public void addEmitter(SseEmitter emitter) {
		emitters.add(emitter);
		try {
			emitter.send(getRanked());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update() {
		for (ParticipantRanking participantRanking : participantRankings.values()) {
			participantRanking.update();
		}
		List<SseEmitter> emittersToRemove = new ArrayList<SseEmitter>();
		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(getRanked());
			} catch (Exception e) {
				logger.error("Error emitting, removing emitter", e);
				emittersToRemove.add(emitter);
			}
		}
		emittersToRemove.forEach(e -> emitters.remove(e));
	}

	@Override
	public Set<String> getAllVoted() {
		return participantRankings.values().stream().flatMap(r -> r.getAllVoted().stream()).collect(Collectors.toSet());
	}

	@Override
	public void renameElement(String element, String newName) {
		for (ParticipantRanking participantRanking : participantRankings.values()) {
			participantRanking.renameElement(element, newName);
		}
		update();
	}
}
