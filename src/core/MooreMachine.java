package core;

import java.io.PrintWriter;

public class MooreMachine implements Cloneable {
	private int startState;
	private int significantMask;
	private int[][] nextState;
	private Turn[] moves;

	private String getBitString(int mask, int length) {
		char[] ans = new char[length];
		for (int i = 0; i < length; ++i) {
			ans[length - 1 - i] = (char) ('0' + ((mask >> i) & 1));
		}
		return new String(ans);
	}

	public MooreMachine(int startState, int significantMask, int[][] nextState,
			Turn[] moves) throws InvalidAutomatonException {
		final int sign = Constants.SIGNIFICANT_INPUTS;
		final int states = Constants.STATES_NUMBER;
		final int vis = Constants.VISIBLE_CELLS;
		if (Integer.bitCount(significantMask) != sign
				|| (significantMask >> vis) != 0 || moves.length != states
				|| nextState.length != states) {
			throw new InvalidAutomatonException();
		}
		for (int i = 0; i < nextState.length; ++i) {
			if (nextState[i].length != (1 << sign)) {
				throw new InvalidAutomatonException();
			}
		}
		this.startState = startState;
		this.significantMask = significantMask;
		this.nextState = nextState.clone();
		this.moves = moves.clone();
	}

	@Override
	public MooreMachine clone() {
		try {
			return new MooreMachine(startState, significantMask, nextState,
					moves);
		} catch (InvalidAutomatonException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getStartState() {
		return startState;
	}

	public int getSignificantMask() {
		return significantMask;
	}

	public Turn getMove(int state) {
		return moves[state];
	}

	public int getNextState(int state, int mask) {
		return nextState[state][mask];
	}

	public int[][] getNextStateArray() {
		return nextState.clone();
	}

	public Turn[] getMovesArray() {
		return moves;
	}

	public void print(PrintWriter out) {
		out.println("Start state = " + startState);
		out.print("Significant inputs");
		final int vis = Constants.VISIBLE_CELLS;
		for (int i = 0; i < vis; ++i) {
			if (((significantMask >> i) & 1) == 1) {
				out.print(" " + i);
			}
		}
		out.println();
		final int states = Constants.STATES_NUMBER;
		out.print("Moves:");
		for (int i = 0; i < states; ++i) {
			out.print(" " + moves[i]);
		}
		out.println();
		out.printf("%15s%15s%15s", "state", "inputs", "next state");
		out.println();
		final int sign = Constants.SIGNIFICANT_INPUTS;
		for (int i = 0; i < states; ++i) {
			for (int j = 0; j < (1 << sign); ++j) {
				out.printf("%15d%15s%15d", i, getBitString(j,
						Constants.SIGNIFICANT_INPUTS), nextState[i][j]);
				out.println();
			}
		}
	}
}
