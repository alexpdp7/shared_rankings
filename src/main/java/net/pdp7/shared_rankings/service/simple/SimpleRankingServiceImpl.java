package net.pdp7.shared_rankings.service.simple;

import java.util.ArrayList;
import java.util.Collection;

import net.pdp7.shared_rankings.service.RankingService;

public class SimpleRankingServiceImpl implements RankingService {

	public final Collection<String> rankings = new ArrayList<String>();

	public SimpleRankingServiceImpl(boolean testData) {
		if (testData) {
			rankings.add("test");
		}
	}

	@Override
	public Collection<String> getRankings() {
		return rankings;
	}

	@Override
	public void addRanking(String newRanking) {
		rankings.add(newRanking);
	}
}
