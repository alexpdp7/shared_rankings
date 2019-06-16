package net.pdp7.shared_rankings.service.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import net.pdp7.shared_rankings.service.ParticipantRanking;
import net.pdp7.shared_rankings.service.Ranking;
import net.pdp7.shared_rankings.service.RankingService;

public class SimpleRankingServiceImpl implements RankingService {

	public final Collection<Ranking> rankings = new ArrayList<Ranking>();

	public SimpleRankingServiceImpl(boolean testData) {
		if (testData) {
			SimpleRankingImpl ranking = new SimpleRankingImpl("test");

			ParticipantRanking aliceRanking = new SimpleParticipantRankingImpl();
			try {
				aliceRanking.setElements(
						List.of(List.of("Blade Runner", "The Princess Bride"), List.of("Manos the Hands of Fate")));
				ranking.addParticipant("Alice", aliceRanking);

				ParticipantRanking bobRanking = new SimpleParticipantRankingImpl();
				bobRanking.setElements(
						List.of(List.of("Blade Runner", "The Princess Bride"), List.of("Manos the Hands of Fate")));
				ranking.addParticipant("Bob", bobRanking);

				ParticipantRanking carolRanking = new SimpleParticipantRankingImpl();
				carolRanking.setElements(
						List.of(List.of("Blade Runner", "The Princess Bride"), List.of("Manos the Hands of Fate")));
				ranking.addParticipant("Carol", carolRanking);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			rankings.add(ranking);
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
