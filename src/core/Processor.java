package core;

import java.util.ArrayList;
import java.util.Collections;

import ec.util.MersenneTwister;
import main.ResultSaver;

public class Processor {
	private static void updateGeneration(int genNumber,
			SimulationResult[] best, ResultSaver rs, double[] prob) {
		final int size = Constants.GENERATION_SIZE;
		final MersenneTwister rand = Constants.rand;
		final int elite = (int) (size * Constants.ELITE_PART);
		ArrayList<SimulationResult> candidates = new ArrayList<SimulationResult>();
		for (int i = 0; i < size; ++i) {
			int other = rand.nextInt(size);
			while (i == other) {
				other = rand.nextInt(size);
			}
			candidates.add(new SimulationResult(best[i].auto
					.crossover(best[other].auto), 0., 0));
		}
		for (int i = 0; i < elite; ++i) {
			candidates.add(best[i]);
		}
		for (int i = elite; i < size; ++i) {
			MooreMachine current = best[i].auto;
			for (Mutation m : Mutation.values()) {
				if (rand.nextDouble() <= prob[m.ordinal()]) {
					current.mutate(m);
				}
			}
			candidates.add(new SimulationResult(current, 0., 0));
		}
		for (int i = 0; i < Constants.FIELDS_IN_GENERATION; ++i) {
			FitnessCounter.updateFitness(candidates, new Field());
		}
		Collections.sort(candidates);
		double meanPart = 0.;
		for (int i = 0; i < size; ++i) {
			best[i] = candidates.get(i);
			meanPart += best[i].eatenPartsSum / best[i].fieldsTested;
		}
		meanPart /= size;
		double bestPart = best[0].eatenPartsSum / best[0].fieldsTested;
		rs.saveGeneration(genNumber, bestPart, meanPart);
	}

	public static void run(double[] prob) {
		final int size = Constants.GENERATION_SIZE;
		ResultSaver rs = new ResultSaver(prob);
		SimulationResult[] best = new SimulationResult[size];
		for (int j = 0; j < size; ++j) {
			best[j] = new SimulationResult(new MooreMachine(), 0., 0);
		}
		for (int j = 0; j < Constants.ITERATIONS; ++j) {
			updateGeneration(j + 1, best, rs, prob);
		}
		rs.saveAutomaton(best[0]);
		rs.close();
	}
}
