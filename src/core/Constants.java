package core;

import ec.util.MersenneTwister;

public class Constants {
	public static final MersenneTwister rand = new MersenneTwister(System
			.nanoTime());
	public static final int THREADS = 2;
	public static int RUNNINGS_PER_THREAD = 5;
	public static final int TURNS_NUMBER = 200;
	public static final int FIELD_SIZE = 32;
	public static final int START_ROW = 0;
	public static final int START_COLUMN = 0;
	public static final Direction START_DIRECTION = Direction.RIGHT;
	public static final double[] MUTATION_PROBABILITIES = new double[] { 0.01,
			0.05, 0.1, 0.15, 0.2 };
	static final int STATES_NUMBER = 10;
	static final int SIGNIFICANT_INPUTS = 4;
	static final int GENERATION_SIZE = 50;
	static final double ELITE_PART = 0.2;
	static final int FIELDS_IN_GENERATION = 50;
	static final double FOOD_PROBABILITY = 0.05;
	static final int VISIBLE_CELLS = 8;
	static final int ITERATIONS = 3000;

}
