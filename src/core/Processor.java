package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import ec.util.MersenneTwister;

public class Processor {
	private static void updateGeneration(SimulationResult[] previous,
			boolean debug) {
		final int size = Values.GENERATION_SIZE;
		final MersenneTwister rand = Values.rand;
		final int states = Values.STATES_NUMBER;
		final int elite = (int) (size * Values.ELITE_PART);
		ArrayList<SimulationResult> candidates = new ArrayList<SimulationResult>();
		for (int i = 0; i < elite; ++i) {
			candidates.add(previous[i]);
			if (elite > 1) {
				int other = rand.nextInt(elite);
				while (other == i) {
					other = rand.nextInt(elite);
				}
				candidates.add(new SimulationResult(previous[i].auto
						.crossover(previous[other].auto), 0., 0));
			}
		}
		for (int i = elite; i < size; ++i) {
			MooreMachine current = previous[i].auto;
			if (states > 1 && rand.nextDouble() <= Values.nextStateMutationProb) {
				current = current.nextStateMutation();
			}
			if (rand.nextDouble() <= Values.actionMutationProb) {
				current = current.actionMutation();
			}
			if (Values.SIGNIFICANT_INPUTS < Values.VISIBLE_CELLS
					&& rand.nextDouble() <= Values.significantInputMutationProb) {
				current = current.significantInputMutation();
			}
			if (states > 1
					&& rand.nextDouble() <= Values.startStateMutationProb) {
				current = current.startStateMutation();
			}
			candidates.add(new SimulationResult(current, 0., 0));
		}
		for (int i = 0; i < Values.FIELDS_IN_GENERATION; ++i) {
			FitnessCounter.updateFitness(candidates, new Field());
		}
		Collections.sort(candidates);
		double meanPart = 0.;
		for (int i = 0; i < size; ++i) {
			previous[i] = candidates.get(i);
			meanPart += previous[i].eatenPartsSum / previous[i].fieldsTested;
		}
		meanPart /= size;
		double bestPart = previous[0].eatenPartsSum / previous[0].fieldsTested;
		if (debug) {
			System.out.printf(Locale.US, "%7.3f/%03d%7.3f", bestPart,
					(previous[0].fieldsTested / Values.FIELDS_IN_GENERATION),
					meanPart);
			System.out.println();
		}
	}

	public static void process(boolean debug) {
		final double[] prob = Values.MUTATION_PROBABILITIES;
		final int size = Values.GENERATION_SIZE;
		for (int i = 0; i < 4; ++i) {
			for (double p : prob) {
				double[] probs = new double[4];
				for (int j = 0; j < 4; ++j) {
					probs[j] = (j == i) ? p : Values.DEFAULT_MUTATION_PROB;
				}
				Values.init(probs[0], probs[1], probs[2], probs[3]);
				SimulationResult[] best = new SimulationResult[size];
				for (int m = 0; m < size; ++m) {
					best[m] = new SimulationResult(new MooreMachine(), 0., 0);
				}
				for (int m = 0; m < Values.ITERATIONS; ++m) {
					if (debug) {
						System.out.printf("%6d: ", m + 1);
					}
					updateGeneration(best, debug);
				}
			}
		}
	}
}
