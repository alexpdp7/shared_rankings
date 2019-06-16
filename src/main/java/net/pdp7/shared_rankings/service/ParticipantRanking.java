package net.pdp7.shared_rankings.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ParticipantRanking {
	public List<List<String>> getElements();

	public void setElements(List<List<String>> elements) throws IOException;

	public void addEmitter(SseEmitter sseEmitter);
}
