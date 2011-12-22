package main;

import core.Constants;

public class BestAutomataSearcher {
	public static void main(String[] args) {
		final int thr = Constants.THREADS;
		Thread[] searcher = new Thread[thr];
		for (int i = 0; i < thr; ++i) {
			searcher[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < Constants.SEARCHES_PER_THREAD; ++i) {
						new Processor(Constants.BEST_AUTO_DIR).run(
								Constants.BEST_MUTATION_PROBABILITIES,
								Constants.SEARCHER_ITERATIONS);
					}
				}
			});
		}
		for (int i = 0; i < thr; ++i) {
			searcher[i].start();
		}
	}
}
