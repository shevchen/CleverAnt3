package main;

import java.util.Arrays;

import core.Constants;
import core.Mutation;

public class Runner {
	public static void main(String[] args) throws InterruptedException {
		final int thr = Constants.THREADS;
		Thread[] proc = new Thread[thr];
		for (int i = 0; i < thr; ++i) {
			proc[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < Constants.RUNNINGS_PER_THREAD; ++i) {
						System.out.println("Starting iteration " + (i + 1)
								+ "/" + Constants.RUNNINGS_PER_THREAD);
						for (Mutation m : Mutation.values()) {
							for (double p : Constants.MUTATION_PROBABILITIES) {
								double[] prob = new double[Mutation.values().length];
								Arrays.fill(prob,
										Constants.COMMON_MUTATION_PROBABILITY);
								prob[m.ordinal()] = p;
								final String dirName = Constants.RESULTS_DIR
										+ "/" + m + "/"
										+ ResultSaver.getDirectoryName(prob);
								new Processor(dirName).run(prob,
										Constants.RUNNER_ITERATIONS);
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
