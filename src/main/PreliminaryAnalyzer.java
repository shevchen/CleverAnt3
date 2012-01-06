package main;

import java.util.Arrays;

import core.Constants;
import core.Mutation;

public class PreliminaryAnalyzer {
	public static void main(String[] args) {
		final int thr = Constants.THREADS;
		Thread[] searcher = new Thread[thr];
		for (int i = 0; i < thr; ++i) {
			searcher[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < Constants.SEARCHES_PER_THREAD; ++i) {
						for (double p : Constants.MUTATION_PROBABILITIES) {
							double[] prob = new double[Mutation.values().length];
							Arrays.fill(prob, p);
							final String dirName = Constants.PRELIMINARY_RESULTS_DIR
									+ "/" + ResultSaver.getDirectoryName(prob);
							new Processor(dirName).run(prob,
									Constants.SEARCHER_ITERATIONS);
						}
					}
				}
			});
		}
		for (int i = 0; i < thr; ++i) {
			searcher[i].start();
		}
	}
}
