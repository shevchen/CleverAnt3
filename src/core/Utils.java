package core;

public class Utils {
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

	static int modSize(int n) {
		int size = Values.FIELD_SIZE;
		return (n % size + size) % size;
	}

	public static Cell[] getVisible(int row, int column, Direction dir) {
		Cell[] result = new Cell[Values.VISIBLE_CELLS];
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
