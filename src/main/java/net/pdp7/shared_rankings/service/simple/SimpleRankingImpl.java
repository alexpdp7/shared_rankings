package net.pdp7.shared_rankings.service.simple;

import java.util.ArrayList;
import java.util.Collection;

import net.pdp7.shared_rankings.service.Ranking;

public class SimpleRankingImpl implements Ranking {

	public final String name;
	public final Collection<String> participants = new ArrayList<String>();

	public SimpleRankingImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Collection<String> getParticipants() {
		return participants;
	}

	@Override
	public void addParticipant(String newParticipant) {
		participants.add(newParticipant);
	}

}
