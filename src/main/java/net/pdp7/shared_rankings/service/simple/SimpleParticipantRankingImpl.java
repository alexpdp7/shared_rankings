package net.pdp7.shared_rankings.service.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import net.pdp7.shared_rankings.service.ParticipantRanking;
import net.pdp7.shared_rankings.service.Ranking;

public class SimpleParticipantRankingImpl implements ParticipantRanking {

	protected List<List<String>> elements = new ArrayList<List<String>>();
	protected List<SseEmitter> emitters = new ArrayList<SseEmitter>();

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected final Ranking ranking;

	public SimpleParticipantRankingImpl(Ranking ranking) {
		this.ranking = ranking;
	}

	@Override
	public List<List<String>> getElements() {
		Set<String> allVoted = ranking.getAllVoted();
		allVoted.removeAll(getAllVoted());
		ArrayList<List<String>> elementsWithRest = new ArrayList<List<String>>(elements);
		if (!allVoted.isEmpty()) {
			elementsWithRest.add(new ArrayList<String>(allVoted));
		}
		return elementsWithRest;
	}

	@Override
	public Set<String> getAllVoted() {
		return elements.stream().flatMap(r -> r.stream()).collect(Collectors.toSet());
	}

	@Override
	public void setElements(List<List<String>> elements) {
		this.elements = elements;
		ranking.update();
	}

	@Override
	public void update() {
		List<SseEmitter> emittersToRemove = new ArrayList<SseEmitter>();
		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(getElements());
			} catch (Exception e) {
				logger.error("Error emitting, removing emitter", e);
				emittersToRemove.add(emitter);
			}
		}
		emittersToRemove.forEach(e -> emitters.remove(e));
	}

	@Override
	public void addEmitter(SseEmitter sseEmitter) {
		emitters.add(sseEmitter);
		try {
			sseEmitter.send(getElements());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void renameElement(String element, String newName) {
		setElements(elements.stream()
				.map(r -> r.stream().map(e -> e.equals(element) ? newName : e).collect(Collectors.toList()))
				.collect(Collectors.toList()));
	}

	@Override
	public void deleteElement(String element) {
		setElements(elements.stream().map(r -> r.stream().filter(e -> !e.equals(element)).collect(Collectors.toList()))
				.filter(r -> !r.isEmpty()).collect(Collectors.toList()));
	}
}
