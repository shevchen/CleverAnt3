package core;

import java.io.PrintWriter;

import ec.util.MersenneTwister;

public class Field {
	private boolean[][] field;
	private int totalFood;

	public boolean hasFood(int row, int column) {
		return field[row][column];
	}

	private static int modSize(int n) {
		final int size = Constants.FIELD_SIZE;
		if (n < 0) {
			n += size;
		}
		if (n >= size) {
			n -= size;
		}
		return n;
	}

	public static Cell[] getVisible(int row, int column, Direction dir) {
		Cell[] result = new Cell[Constants.VISIBLE_CELLS];
		switch (dir) {
		case LEFT:
			// __7
			// _65
			// 43x
			// _21
			// __0
			result[0] = new Cell(modSize(row + 2), column);
			result[1] = new Cell(modSize(row + 1), column);
			result[2] = new Cell(modSize(row + 1), modSize(column - 1));
			result[3] = new Cell(row, modSize(column - 1));
			result[4] = new Cell(row, modSize(column - 2));
			result[5] = new Cell(modSize(row - 1), column);
			result[6] = new Cell(modSize(row - 1), modSize(column - 1));
			result[7] = new Cell(modSize(row - 2), column);
			return result;
		case RIGHT:
			// 0__
			// 12_
			// x34
			// 56_
			// 7__
			result[0] = new Cell(modSize(row - 2), column);
			result[1] = new Cell(modSize(row - 1), column);
			result[2] = new Cell(modSize(row - 1), modSize(column + 1));
			result[3] = new Cell(row, modSize(column + 1));
			result[4] = new Cell(row, modSize(column + 2));
			result[5] = new Cell(modSize(row + 1), column);
			result[6] = new Cell(modSize(row + 1), modSize(column + 1));
			result[7] = new Cell(modSize(row + 2), column);
			return result;
		case UP:
			// __4__
			// _236_
			// 01x57
			result[0] = new Cell(row, modSize(column - 2));
			result[1] = new Cell(row, modSize(column - 1));
			result[2] = new Cell(modSize(row - 1), modSize(column - 1));
			result[3] = new Cell(modSize(row - 1), column);
			result[4] = new Cell(modSize(row - 2), column);
			result[5] = new Cell(row, modSize(column + 1));
			result[6] = new Cell(modSize(row - 1), modSize(column + 1));
			result[7] = new Cell(row, modSize(column + 2));
			return result;
		case DOWN:
			// 75x10
			// _632_
			// __4__
			result[0] = new Cell(row, modSize(column + 2));
			result[1] = new Cell(row, modSize(column + 1));
			result[2] = new Cell(modSize(row + 1), modSize(column + 1));
			result[3] = new Cell(modSize(row + 1), column);
			result[4] = new Cell(modSize(row + 2), column);
			result[5] = new Cell(row, modSize(column - 1));
			result[6] = new Cell(modSize(row + 1), modSize(column - 1));
			result[7] = new Cell(row, modSize(column - 2));
			return result;
		}
		throw new RuntimeException();
	}

	private static int getNextRow(int row, Direction dir) {
		switch (dir) {
		case LEFT:
			return row;
		case RIGHT:
			return row;
		case UP:
			return modSize(row - 1);
		case DOWN:
			return modSize(row + 1);
		}
		throw new RuntimeException();
	}

	private static int getNextColumn(int column, Direction dir) {
		switch (dir) {
		case LEFT:
			return modSize(column - 1);
		case RIGHT:
			return modSize(column + 1);
		case UP:
			return column;
		case DOWN:
			return column;
		}
		throw new RuntimeException();
	}

	private static int getVisibleMask(int row, int column, Direction dir,
			boolean[][] left) {
		Cell[] cells = getVisible(row, column, dir);
		int result = 0;
		for (int i = 0; i < cells.length; ++i) {
			result |= ((left[cells[i].row][cells[i].column] ? 1 : 0) << i);
		}
		return result;
	}

	private static int getActualMask(int fullMask, int signMask) {
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
				if (i == Constants.START_ROW && j == Constants.START_COLUMN) {
					continue;
				}
				field[i][j] = rand.nextDouble() < Constants.FOOD_PROBABILITY;
				if (field[i][j]) {
					++totalFood;
				}
			}
		}
	}

	public int getTotalFood() {
		return totalFood;
	}

	public static boolean makeStep(MooreMachine auto, AntState as,
			boolean[][] curField) {
		int visibleMask = getVisibleMask(as.row, as.column, as.dir, curField);
		Turn action = auto.getMove(as.autoState);
		as.autoState = auto.getNextState(as.autoState, getActualMask(
				visibleMask, auto.getSignificantMask()));
		switch (action) {
		case MOVE:
			as.row = getNextRow(as.row, as.dir);
			as.column = getNextColumn(as.column, as.dir);
			if (curField[as.row][as.column]) {
				curField[as.row][as.column] = false;
				++as.eaten;
				return true;
			}
			return false;
		case ROTATELEFT:
			as.dir = as.dir.rotateLeft();
			return false;
		case ROTATERIGHT:
			as.dir = as.dir.rotateRight();
			return false;
		}
		throw new RuntimeException();
	}

	public static int simulate(MooreMachine auto, Field f) {
		final int size = Constants.FIELD_SIZE;
		boolean[][] curField = new boolean[size][size];
		for (int i = 0; i < Constants.FIELD_SIZE; ++i) {
			System.arraycopy(f.field[i], 0, curField[i], 0,
					Constants.FIELD_SIZE);
		}
		AntState antState = new AntState(auto.getStartState(),
				Constants.START_ROW, Constants.START_COLUMN,
				Constants.START_DIRECTION, 0);
		for (int i = 0; i < Constants.TURNS_NUMBER; ++i) {
			makeStep(auto, antState, curField);
		}
		return antState.eaten;
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
