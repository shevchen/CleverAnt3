package main;

import java.util.List;

import core.Field;
import core.SimulationResult;

public class FitnessCounter {
	public static void updateFitness(List<SimulationResult> generation, Field f) {
		for (SimulationResult sr : generation) {
			sr.eatenPartsSum += 1. * f.simulate(sr.auto) / f.getTotalFood();
			sr.fieldsTested++;
		}
	}
}
