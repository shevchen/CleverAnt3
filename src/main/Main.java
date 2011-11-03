package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

import core.AutomataChanger;
import core.Constants;
import core.Field;
import core.MooreMachine;
import core.MooreMachineGenerator;
import core.SimulationResult;
import ec.util.MersenneTwisterFast;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File("test.txt"));
		final int size = Constants.GENERATION_SIZE;
		MooreMachine[] best = new MooreMachine[size];
		for (int i = 0; i < size; ++i) {
			best[i] = MooreMachineGenerator.generate();
		}
		final MersenneTwisterFast rand = Constants.rand;
		for (int i = 0; i < Constants.ITERATIONS; ++i) {
			MooreMachine[] candidates = new MooreMachine[3 * size];
			for (int j = 0; j < size; ++j) {
				candidates[j] = best[j];
				candidates[size + j] = AutomataChanger.mutate(best[j]);
				int other = rand.nextInt(size);
				while (other == j) {
					other = rand.nextInt(size);
				}
				candidates[2 * size + j] = AutomataChanger.crossover(best[j],
						best[other]);
			}
			int[] sum = new int[3 * size];
			for (int j = 0; j < Constants.FIELDS_IN_GENERATION; ++j) {
				int[] cur = FitnessCounter.getFitness(candidates, new Field());
				for (int k = 0; k < 3 * size; ++k) {
					sum[k] += cur[k];
				}
			}
			SimulationResult[] result = new SimulationResult[3 * size];
			for (int j = 0; j < 3 * size; ++j) {
				result[j] = new SimulationResult(candidates[j], sum[j]);
			}
			Arrays.sort(result, Constants.simulationResultComparator);
			for (int j = 0; j < size; ++j) {
				candidates[j] = result[j].auto;
			}
			System.err
					.println((int) (result[0].eaten * 1.0 / Constants.FIELDS_IN_GENERATION));
		}
		out.close();
	}
}
