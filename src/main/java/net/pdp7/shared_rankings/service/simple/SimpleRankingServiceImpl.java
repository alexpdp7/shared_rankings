package net.pdp7.shared_rankings.service.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import net.pdp7.shared_rankings.service.Ranking;
import net.pdp7.shared_rankings.service.RankingService;

public class SimpleRankingServiceImpl implements RankingService {

	public final Collection<Ranking> rankings = new ArrayList<Ranking>();

	public SimpleRankingServiceImpl(boolean testData) {
		if (testData) {
			rankings.add(new SimpleRankingImpl("test"));
		}
	}

	@Override
	public Collection<String> getRankings() {
		return rankings.stream().map(Ranking::getName).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public void addRanking(String newRanking) {
		rankings.add(new SimpleRankingImpl(newRanking));
	}

	@Override
	public Ranking getRanking(String ranking) {
		return rankings.stream().filter(r -> r.getName().equals(ranking)).findFirst().get();
	}
}
