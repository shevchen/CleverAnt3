package core;

public class SimulationResult implements Comparable<SimulationResult> {
	private MooreMachine auto;
	private double eatenPartsSum;
	private int fieldsTested;

	public SimulationResult(MooreMachine auto, double eatenPartsSum,
			int fieldsTested) {
		this.auto = auto;
		this.eatenPartsSum = eatenPartsSum;
		this.fieldsTested = fieldsTested;
	}

	public MooreMachine getAuto() {
		return auto;
	}

	public void addPart(double part) {
		eatenPartsSum += part;
	}

	public void addFields(int fields) {
		fieldsTested += fields;
	}

	public double getMeanEatenPart() {
		return eatenPartsSum / fieldsTested;
	}

	@Override
	public int compareTo(SimulationResult o) {
		return Double.compare(o.eatenPartsSum / o.fieldsTested, eatenPartsSum
				/ fieldsTested);
	}
}
