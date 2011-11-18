package core;

public class AntState {
	private int row, column;
	private Direction dir;

	public AntState(int row, int column, Direction dir) {
		this.row = row;
		this.column = column;
		this.dir = dir;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Direction getDirection() {
		return dir;
	}
}
