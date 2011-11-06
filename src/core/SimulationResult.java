package core;

public class SimulationResult implements Comparable<SimulationResult> {
	public MooreMachine auto;
	public double eatenPartsSum;
	public int fieldsTested;

	public SimulationResult(MooreMachine auto, double eatenPartsSum,
			int fieldsTested) {
		this.auto = auto;
		this.eatenPartsSum = eatenPartsSum;
		this.fieldsTested = fieldsTested;
	}

	@Override
	public int compareTo(SimulationResult o) {
		return Double.compare(o.eatenPartsSum / o.fieldsTested, eatenPartsSum
				/ fieldsTested);
	}
}
