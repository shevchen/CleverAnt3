package main;

import java.util.Arrays;

import core.Constants;
import core.Field;
import core.MooreMachine;
import core.Mutation;
import core.SimulationResult;

public class Processor {
	private Field[] fields;
	private SimulationResult best;
	private ResultSaver rs;
	private double[] prob;

	public Processor(String dirName) {
		this.rs = new ResultSaver(prob, dirName);
	}

	private void updateGeneration(int genNumber) {
		MooreMachine current = best.getAuto().clone();
		for (Mutation m : Mutation.values()) {
			current.mutate(m, prob[m.ordinal()]);
		}
		SimulationResult sr = new SimulationResult(current, 0., 0);
		for (Field f : fields) {
			FitnessCounter.updateFitness(sr, f);
		}
		if (sr.compareTo(best) < 0) {
			best = sr;
		}
		double part = best.getMeanEatenPart();
		rs.saveGeneration(genNumber, part);
	}

	public void run(double[] prob, final int iterations) {
		this.prob = prob;
		final int fieldsN = Constants.FIELDS_IN_GENERATION;
		fields = new Field[fieldsN];
		for (int i = 0; i < fieldsN; ++i) {
			fields[i] = new Field();
		}
		best = new SimulationResult(new MooreMachine(), 0., 0);
		for (Field f : fields) {
			FitnessCounter.updateFitness(best, f);
		}
		for (int j = 0; j < iterations; ++j) {
			updateGeneration(j + 1);
		}
		rs.saveAutomaton(best);
		rs.close();
		System.out.println("Done " + Arrays.toString(prob));
	}
}
