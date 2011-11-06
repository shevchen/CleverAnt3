package core;

import ec.util.MersenneTwisterFast;

public class Constants {
	public static final int STATES_NUMBER = 7;
	public static final int SIGNIFICANT_INPUTS = 2;
	public static final int FIELD_SIZE = 32;
	public static final int GENERATION_SIZE = 100;
	public static final int FIELDS_IN_GENERATION = 100;
	public static final int ITERATIONS = 100000;
	public static final int TURNS_NUMBER = 200;
	public static final int START_ROW = 0;
	public static final int START_COLUMN = 0;
	public static final Direction START_DIRECTION = Direction.RIGHT;
	public static final int VISIBLE_CELLS = 8;
	public static final double FOOD_PROBABILITY = 0.05;
	public static final double NEXT_STATE_MUTATION_PROB = 0.2;
	public static final double ACTION_MUTATION_PROB = 0.2;
	public static final double SIGNIFICANT_INPUT_MUTATION_PROB = 0.2;
	public static final double START_STATE_MUTATION_PROB = 0.2;

	public static final MersenneTwisterFast rand = new MersenneTwisterFast(
			System.nanoTime());

	public static int[][] clone(int[][] source) {
		int[][] ans = new int[source.length][];
		for (int i = 0; i < source.length; ++i) {
			ans[i] = source[i].clone();
		}
		return ans;
	}

	public static boolean[][] clone(boolean[][] source) {
		boolean[][] ans = new boolean[source.length][];
		for (int i = 0; i < source.length; ++i) {
			ans[i] = source[i].clone();
		}
		return ans;
	}

	public static int modSize(int n) {
		int size = FIELD_SIZE;
		return (n % size + size) % size;
	}

	public static Cell[] getVisible(Cell cell, Direction dir) {
		int row = cell.row;
		int column = cell.column;
		Cell[] result = new Cell[VISIBLE_CELLS];
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
}
