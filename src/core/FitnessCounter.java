package core;

import java.util.ArrayList;

public class FitnessCounter {
	public static void updateFitness(ArrayList<SimulationResult> generation,
			Field f) {
		for (SimulationResult sr : generation) {
			sr.eatenPartsSum += 1. * f.simulate(sr.auto) / f.getTotalFood();
			sr.fieldsTested++;
		}
	}
}
