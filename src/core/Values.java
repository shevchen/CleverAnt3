package core;

import java.util.Arrays;

import ec.util.MersenneTwister;

public class Values {
	public static final MersenneTwister rand = new MersenneTwister(System
			.nanoTime());
	public static final int TURNS_NUMBER = 200;
	public static final int FIELD_SIZE = 32;
	public static final int START_ROW = 0;
	public static final int START_COLUMN = 0;
	public static final Direction START_DIRECTION = Direction.RIGHT;
	static final int STATES_NUMBER = 10;
	static final int SIGNIFICANT_INPUTS = 4;
	static final int GENERATION_SIZE = 100;
	static final double ELITE_PART = 0.2;
	static final int FIELDS_IN_GENERATION = 100;
	static final double FOOD_PROBABILITY = 0.05;
	static final int VISIBLE_CELLS = 8;
	static final int ITERATIONS = 20000;
	static final int RUNNINGS = 10;
	static final double DEFAULT_MUTATION_PROB = 0.12;
	static final double[] MUTATION_PROBABILITIES = new double[] { 0.05, 0.1,
			0.2 };

	static double nextStateMutationProb;
	static double actionMutationProb;
	static double significantInputMutationProb;
	static double startStateMutationProb;

	static double bestFitness[] = new double[ITERATIONS];
	static double meanFitness[] = new double[ITERATIONS];

	public static void init(double nextMut, double actMut, double inputMut,
			double startMut) {
		nextStateMutationProb = nextMut;
		actionMutationProb = actMut;
		significantInputMutationProb = inputMut;
		startStateMutationProb = startMut;
		Arrays.fill(bestFitness, 0);
		Arrays.fill(meanFitness, 0);
	}
}
