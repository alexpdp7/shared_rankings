package net.pdp7.shared_rankings.service;

import java.util.Collection;

public interface Ranking {
	public String getName();

	public Collection<String> getParticipants();

	public void addParticipant(String newParticipant, ParticipantRanking participantRanking);

	public ParticipantRanking getParticipantRanking(String participant);
}
