package core;

public class SimulationResult {
	public int eaten;
	public int lastSuccessfulMove;

	public SimulationResult(int eaten, int lastSuccessfulMove) {
		this.eaten = eaten;
		this.lastSuccessfulMove = lastSuccessfulMove;
	}
}
