package core;

public class AntState implements Cloneable {
	public int autoState;
	public int row, column;
	public Direction dir;
	public int eaten;

	public AntState(int currentState, int currentRow, int currentColumn,
			Direction currentDir, int eaten) {
		this.autoState = currentState;
		this.row = currentRow;
		this.column = currentColumn;
		this.dir = currentDir;
		this.eaten = eaten;
	}

	@Override
	public AntState clone() {
		return new AntState(autoState, row, column, dir, eaten);
	}
}
