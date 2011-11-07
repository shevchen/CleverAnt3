package core;

import java.io.PrintWriter;

import ec.util.MersenneTwister;

public class Field {
	private boolean[][] field;
	private int totalFood;

	private int getNextRow(int row, Direction dir) {
		switch (dir) {
		case LEFT:
			return row;
		case RIGHT:
			return row;
		case UP:
			return Constants.modSize(row - 1);
		case DOWN:
			return Constants.modSize(row + 1);
		}
		throw new RuntimeException();
	}

	private int getNextColumn(int column, Direction dir) {
		switch (dir) {
		case LEFT:
			return Constants.modSize(column - 1);
		case RIGHT:
			return Constants.modSize(column + 1);
		case UP:
			return column;
		case DOWN:
			return column;
		}
		throw new RuntimeException();
	}

	private int getVisibleMask(int row, int column, Direction dir,
			boolean[][] left) {
		Cell[] cells = Constants.getVisible(row, column, dir);
		int result = 0;
		for (int i = 0; i < cells.length; ++i) {
			result |= ((left[cells[i].row][cells[i].column] ? 1 : 0) << i);
		}
		return result;
	}

	private int getActualMask(int fullMask, int signMask) {
		final int vis = Constants.VISIBLE_CELLS;
		int currentBit = 0;
		int result = 0;
		for (int i = 0; i < vis; ++i) {
			if (((signMask >> i) & 1) == 1) {
				result |= ((fullMask >> i) & 1) << (currentBit++);
			}
		}
		return result;
	}

	public Field() {
		final int size = Constants.FIELD_SIZE;
		field = new boolean[size][size];
		final MersenneTwister rand = Constants.rand;
		totalFood = 0;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				field[i][j] = rand.nextDouble() <= Constants.FOOD_PROBABILITY;
				if (field[i][j]) {
					++totalFood;
				}
			}
		}
	}

	public int getTotalFood() {
		return totalFood;
	}

	public int simulate(final MooreMachine auto) {
		int eaten = 0;
		int currentState = auto.getStartState();
		Direction currentDir = Constants.START_DIRECTION;
		boolean[][] left = Constants.clone(field);
		int curRow = Constants.START_ROW;
		int curCol = Constants.START_COLUMN;
		if (left[curRow][curCol]) {
			left[curRow][curCol] = false;
			++eaten;
		}
		final int signMask = auto.getSignificantMask();
		for (int i = 1; i <= Constants.TURNS_NUMBER; ++i) {
			int visibleMask = getVisibleMask(curRow, curCol, currentDir, left);
			Turn action = auto.getMove(currentState);
			currentState = auto.getNextState(currentState, getActualMask(
					visibleMask, signMask));
			switch (action) {
			case MOVE:
				curRow = getNextRow(curRow, currentDir);
				curCol = getNextColumn(curCol, currentDir);
				if (left[curRow][curCol]) {
					left[curRow][curCol] = false;
					++eaten;
				}
			case ROTATELEFT:
				currentDir = currentDir.rotateLeft();
			case ROTATERIGHT:
				currentDir = currentDir.rotateRight();
			}
		}
		return eaten;
	}

	public void print(PrintWriter out) {
		final int size = Constants.FIELD_SIZE;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				out.print(field[i][j] ? 'x' : '.');
			}
			out.println();
		}
	}
}
