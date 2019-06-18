package net.pdp7.shared_rankings.service.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		return elements;
	}

	@Override
	public void setElements(List<List<String>> elements) {
		this.elements = elements;
		List<SseEmitter> emittersToRemove = new ArrayList<SseEmitter>();
		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(elements);
			} catch (Exception e) {
				logger.error("Error emitting, removing emitter", e);
				emittersToRemove.add(emitter);
			}
		}
		emittersToRemove.forEach(e -> emitters.remove(e));
		ranking.updateRanked();
	}

	@Override
	public void addEmitter(SseEmitter sseEmitter) {
		emitters.add(sseEmitter);
		try {
			sseEmitter.send(elements);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
