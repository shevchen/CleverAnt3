package main;

import core.Field;
import core.SimulationResult;

public class FitnessCounter {
	public static void updateFitness(SimulationResult sr, Field f) {
		sr.addPart(1. * Field.simulate(sr.getAuto(), f) / f.getTotalFood());
		sr.addFields(1);
	}
}
