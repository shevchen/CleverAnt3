package core;

public class AntState {
	private int currentState;
	private int currentRow, currentColumn;
	private Direction currentDir;

	public AntState(int currentState, int currentRow, int currentColumn,
			Direction currentDir) {
		this.currentState = currentState;
		this.currentRow = currentRow;
		this.currentColumn = currentColumn;
		this.currentDir = currentDir;
	}

	public int getCurrentState() {
		return currentState;
	}

	public int getCurrentRow() {
		return currentRow;
	}

	public int getCurrentColumn() {
		return currentColumn;
	}

	public Direction getCurrentDir() {
		return currentDir;
	}
}
