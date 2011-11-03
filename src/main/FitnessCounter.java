package main;

import core.Field;
import core.MooreMachine;

public class FitnessCounter {
	public static int[] getFitness(MooreMachine[] generation, Field f) {
		int len = generation.length;
		int[] result = new int[len];
		for (int i = 0; i < len; ++i) {
			result[i] = f.simulate(generation[i]);
		}
		return result;
	}
}
