package main;

import core.Constants;
import core.Field;
import core.MooreMachine;
import core.Mutation;
import core.SimulationResult;
import ec.util.MersenneTwister;

public class Processor {
	private Field[] fields;
	private SimulationResult best;
	private ResultSaver rs;
	private double[] prob;

	private void updateGeneration(int genNumber) {
		final MersenneTwister rand = Constants.rand;
		MooreMachine current = best.getAuto().clone();
		for (Mutation m : Mutation.values()) {
			if (rand.nextDouble() < prob[m.ordinal()]) {
				current.mutate(m);
			}
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

	public void run(double[] prob, final int iterations, final String dirName) {
		this.prob = prob;
		final int fieldsN = Constants.FIELDS_IN_GENERATION;
		fields = new Field[fieldsN];
		for (int i = 0; i < fieldsN; ++i) {
			fields[i] = new Field();
		}
		rs = new ResultSaver(prob, dirName);
		best = new SimulationResult(new MooreMachine(), 0., 0);
		for (Field f : fields) {
			FitnessCounter.updateFitness(best, f);
		}
		for (int j = 0; j < iterations; ++j) {
			updateGeneration(j + 1);
		}
		rs.saveAutomaton(best);
		rs.close();
	}
}
