package core;

public class SimulationResult {
	public MooreMachine auto;
	public int eaten;
	public int lastSuccessfulMove;

	public SimulationResult(MooreMachine auto, int eaten, int lastSuccessfulMove) {
		this.auto = auto;
		this.eaten = eaten;
		this.lastSuccessfulMove = lastSuccessfulMove;
	}
}
