package net.pdp7.shared_rankings.service.simple;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.pdp7.shared_rankings.service.ParticipantRanking;
import net.pdp7.shared_rankings.service.Ranking;

public class SimpleRankingImpl implements Ranking {

	public final String name;
	public final Map<String, ParticipantRanking> participantRankings = new HashMap<String, ParticipantRanking>();

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
}
