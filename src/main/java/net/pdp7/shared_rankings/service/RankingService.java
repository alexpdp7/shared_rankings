package net.pdp7.shared_rankings.service;

import java.util.Collection;

public interface RankingService {
	public Collection<String> getRankings();

	public void addRanking(String newRanking);
}
