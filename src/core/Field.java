package core;

import java.io.PrintWriter;

import ec.util.MersenneTwisterFast;

public class Field {
	private boolean[][] field;

	private boolean has(boolean[][] left, Cell cell) {
		return left[cell.row][cell.column];
	}

	private Cell getNext(Cell cell, Direction dir) {
		int row = cell.row;
		int column = cell.column;
		switch (dir) {
		case LEFT:
			return new Cell(row, Constants.modSize(column - 1));
		case RIGHT:
			return new Cell(row, Constants.modSize(column + 1));
		case UP:
			return new Cell(Constants.modSize(row - 1), column);
		case DOWN:
			return new Cell(Constants.modSize(row + 1), column);
		}
		throw new RuntimeException();
	}

	private int getVisibleMask(Cell cell, Direction dir, boolean[][] left) {
		Cell[] cells = Constants.getVisible(cell, dir);
		int result = 0;
		for (int i = 0; i < cells.length; ++i) {
			result |= ((has(left, cells[i]) ? 1 : 0) << i);
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
		final MersenneTwisterFast rand = Constants.rand;
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				field[i][j] = rand.nextDouble() <= Constants.FOOD_PROBABILITY;
			}
		}
	}

	public int simulate(final MooreMachine auto) {
		int eaten = 0;
		int currentState = auto.getStartState();
		Direction currentDir = Constants.START_DIRECTION;
		boolean[][] left = Constants.clone(field);
		Cell currentCell = new Cell(Constants.START_ROW, Constants.START_COLUMN);
		if (has(left, currentCell)) {
			left[currentCell.row][currentCell.column] = false;
			++eaten;
		}
		final int signMask = auto.getSignificantMask();
		for (int i = 1; i <= Constants.TURNS_NUMBER; ++i) {
			int visibleMask = getVisibleMask(currentCell, currentDir, left);
			Turn action = auto.getMove(currentState);
			currentState = auto.getNextState(currentState, getActualMask(
					visibleMask, signMask));
			switch (action) {
			case MOVE:
				currentCell = getNext(currentCell, currentDir);
				if (left[currentCell.row][currentCell.column]) {
					left[currentCell.row][currentCell.column] = false;
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
