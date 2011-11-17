package main;

import core.Mutation;
import core.Processor;

public class Runner {
	public static void main(String[] args) throws InterruptedException {
		final int thr = Constants.THREADS;
		Thread[] proc = new Thread[thr];
		for (int i = 0; i < thr; ++i) {
			proc[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < Constants.RUNNINGS_PER_THREAD; ++i) {
						for (Mutation m : Mutation.values()) {
							for (double p : Constants.MUTATION_PROBABILITIES) {
								double[] prob = new double[Mutation.values().length];
								prob[m.ordinal()] = p;
								final String dirName = Constants.RESULTS_DIR
										+ "/"
										+ ResultSaver.getDirectoryName(prob);
								Processor.run(prob, Constants.ITERATIONS,
										dirName);
							}
						}
					}
				}
			});
		}
		for (int i = 0; i < thr; ++i) {
			proc[i].start();
		}
	}
}
