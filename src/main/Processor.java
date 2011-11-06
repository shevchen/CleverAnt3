package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import core.Constants;
import core.Field;
import core.FitnessCounter;
import core.MooreMachine;
import core.SimulationResult;

import ec.util.MersenneTwisterFast;

public class Processor {
	public static void process() {
		final int size = Constants.GENERATION_SIZE;
		SimulationResult[] best = new SimulationResult[size];
		for (int i = 0; i < size; ++i) {
			best[i] = new SimulationResult(new MooreMachine(), 0., 0);
		}
		final MersenneTwisterFast rand = Constants.rand;
		final int states = Constants.STATES_NUMBER;
		for (int i = 0; i < Constants.ITERATIONS; ++i) {
			ArrayList<SimulationResult> candidates = new ArrayList<SimulationResult>();
			for (int j = 0; j < size; ++j) {
				candidates.add(best[j]);
				if (size > 1) {
					int other = rand.nextInt(size);
					while (other == j) {
						other = rand.nextInt(size);
					}
					candidates.add(new SimulationResult(best[j].auto
							.crossover(best[other].auto), 0., 0));
				}
				if (states > 1
						&& rand.nextDouble() <= Constants.NEXT_STATE_MUTATION_PROB) {
					candidates.add(new SimulationResult(best[j].auto
							.nextStateMutation(), 0., 0));
				}
				if (rand.nextDouble() <= Constants.ACTION_MUTATION_PROB) {
					candidates.add(new SimulationResult(best[j].auto
							.actionMutation(), 0., 0));
				}
				if (Constants.SIGNIFICANT_INPUTS < Constants.VISIBLE_CELLS
						&& rand.nextDouble() <= Constants.SIGNIFICANT_INPUT_MUTATION_PROB) {
					candidates.add(new SimulationResult(best[j].auto
							.significantInputMutation(), 0., 0));
				}
				if (states > 1
						&& rand.nextDouble() <= Constants.START_STATE_MUTATION_PROB) {
					candidates.add(new SimulationResult(best[j].auto
							.startStateMutation(), 0., 0));
				}
			}
			for (int j = 0; j < Constants.FIELDS_IN_GENERATION; ++j) {
				FitnessCounter.updateFitness(candidates, new Field());
			}
			Collections.sort(candidates);
			for (int j = 0; j < size; ++j) {
				best[j] = candidates.get(j);
			}
			System.err.printf(Locale.US, "%.3f",
					candidates.get(0).eatenPartsSum
							/ candidates.get(0).fieldsTested);
			System.err.println();
		}
	}
}
