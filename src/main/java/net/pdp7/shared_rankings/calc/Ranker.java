package net.pdp7.shared_rankings.calc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ranker {

	public <T> List<T> rank(List<List<List<T>>> votes) {
		Map<T, Integer> optionIndex = calculateOptionIndex(votes);
		@SuppressWarnings("unchecked")
		T[] options = (T[]) new Object[optionIndex.size()];
		optionIndex.forEach((option, index) -> options[index] = option);
		int[][] voteMatrix = calculateVoteMatrix(votes, optionIndex);
		int[] wins = new int[voteMatrix.length];
		for(int i=0; i<voteMatrix.length; i++) {
			for(int j=i+1; j<voteMatrix.length; j++) {
				if(voteMatrix[i][j] > voteMatrix[j][i]) {
					wins[i]++;
				}
			}
		}
		Arrays.sort(options, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				return wins[optionIndex.get(o2)] - wins[optionIndex.get(o1)];
			}
		});
		return List.of(options);
	}

	protected <T> int[][] calculateVoteMatrix(List<List<List<T>>> votes, Map<T, Integer> optionIndex) {
		int[][] voteMatrix = new int[optionIndex.size()][optionIndex.size()];
		for(List<List<T>> vote : votes) {
			for(int i=0; i<vote.size(); i++) {
				List<T> winners = vote.get(i);
				for(int j=i+1; j<vote.size(); j++) {
					List<T> losers = vote.get(j);
					for(Object winner : winners) {
						for(Object loser : losers) {
							voteMatrix[optionIndex.get(winner)][optionIndex.get(loser)]++;
						}
					}
				}
			}
		}
		return voteMatrix;
	}

	protected <T> Map<T, Integer> calculateOptionIndex(List<List<List<T>>> votes) {
		Map<T, Integer> optionIndex = new HashMap<T, Integer>();

		int index = 0;
		for(List<List<T>> vote : votes) {
			for(List<T> rank : vote) {
				for(T option : rank) {
					if(optionIndex.containsKey(option)) {
						continue;
					}
					optionIndex.put(option, index++);
				}
			}
		}
		Object[] options = new Object[index];
		optionIndex.forEach((option, i) -> options[i] = option);
		return optionIndex;
	}

}
