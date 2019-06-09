package net.pdp7.shared_rankings.service.simple;

import net.pdp7.shared_rankings.service.Ranking;

public class SimpleRankingImpl implements Ranking {

	public final String name;

	public SimpleRankingImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
