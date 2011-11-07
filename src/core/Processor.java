package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import ec.util.MersenneTwister;

public class Processor {
	private static void updateGeneration(SimulationResult[] previous) {
		final int size = previous.length;
		final MersenneTwister rand = Constants.rand;
		final int states = Constants.STATES_NUMBER;
		ArrayList<SimulationResult> candidates = new ArrayList<SimulationResult>();
		for (int j = 0; j < Constants.NEW_FIELDS_IN_GENERATION; ++j) {
			candidates.add(new SimulationResult(new MooreMachine(), 0., 0));
		}
		for (int j = 0; j < size; ++j) {
			candidates.add(previous[j]);
			if (size > 1) {
				int other = rand.nextInt(size);
				while (other == j) {
					other = rand.nextInt(size);
				}
				candidates.add(new SimulationResult(previous[j].auto
						.crossover(previous[other].auto), 0., 0));
			}
			if (states > 1
					&& rand.nextDouble() <= Constants.NEXT_STATE_MUTATION_PROB) {
				candidates.add(new SimulationResult(previous[j].auto
						.nextStateMutation(), 0., 0));
			}
			if (rand.nextDouble() <= Constants.ACTION_MUTATION_PROB) {
				candidates.add(new SimulationResult(previous[j].auto
						.actionMutation(), 0., 0));
			}
			if (Constants.SIGNIFICANT_INPUTS < Constants.VISIBLE_CELLS
					&& rand.nextDouble() <= Constants.SIGNIFICANT_INPUT_MUTATION_PROB) {
				candidates.add(new SimulationResult(previous[j].auto
						.significantInputMutation(), 0., 0));
			}
			if (states > 1
					&& rand.nextDouble() <= Constants.START_STATE_MUTATION_PROB) {
				candidates.add(new SimulationResult(previous[j].auto
						.startStateMutation(), 0., 0));
			}
		}
		for (int j = 0; j < Constants.FIELDS_IN_GENERATION; ++j) {
			FitnessCounter.updateFitness(candidates, new Field());
		}
		Collections.sort(candidates);
		double meanPart = 0.;
		for (int j = 0; j < size; ++j) {
			previous[j] = candidates.get(j);
			meanPart += previous[j].eatenPartsSum / previous[j].fieldsTested;
		}
		meanPart /= size;
		double bestPart = previous[0].eatenPartsSum / previous[0].fieldsTested;
		System.err.printf(Locale.US, "%7.3f/%03d%7.3f", bestPart,
				(previous[0].fieldsTested / Constants.FIELDS_IN_GENERATION),
				meanPart);
		System.err.println();
	}

	public static void process() {
		final int size = Constants.GENERATION_SIZE;
		SimulationResult[] best = new SimulationResult[size];
		for (int i = 0; i < size; ++i) {
			best[i] = new SimulationResult(new MooreMachine(), 0., 0);
		}
		for (int i = 0; i < Constants.ITERATIONS; ++i) {
			System.err.printf("%6d: ", i + 1);
			updateGeneration(best);
		}
	}
}
