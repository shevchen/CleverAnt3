package core;

import java.util.ArrayList;

public class FitnessCounter {
	public static void updateFitness(ArrayList<SimulationResult> generation,
			Field f) {
		for (int i = 0; i < generation.size(); ++i) {
			SimulationResult sr = generation.get(i);
			sr.eatenPartsSum += 1. * f.simulate(sr.auto) / f.getTotalFood();
			sr.fieldsTested++;
		}
	}
}
