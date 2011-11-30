package core;

public class AntState implements Cloneable {
	public int currentState;
	public int currentRow, currentColumn;
	public Direction currentDir;
	public int eaten;

	public AntState(int currentState, int currentRow, int currentColumn,
			Direction currentDir, int eaten) {
		this.currentState = currentState;
		this.currentRow = currentRow;
		this.currentColumn = currentColumn;
		this.currentDir = currentDir;
		this.eaten = eaten;
	}

	@Override
	public AntState clone() {
		return new AntState(currentState, currentRow, currentColumn,
				currentDir, eaten);
	}
}
