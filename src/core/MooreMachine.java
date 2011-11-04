package core;

import java.io.PrintWriter;

import ec.util.MersenneTwisterFast;

public class MooreMachine implements Cloneable {
	private int startState;
	private int significantMask;
	private int[][] nextState;
	private Turn[] moves;

	private MooreMachine(int startState, int significantMask,
			int[][] nextState, Turn[] moves) {
		this.startState = startState;
		this.significantMask = significantMask;
		this.nextState = Constants.clone(nextState);
		this.moves = moves.clone();
	}

	public MooreMachine() {
		final MersenneTwisterFast rand = Constants.rand;
		final int states = Constants.STATES_NUMBER;
		startState = rand.nextInt(states);
		significantMask = 0;
		final int vis = Constants.VISIBLE_CELLS;
		for (int i = 0; i < Constants.SIGNIFICANT_INPUTS; ++i) {
			int currentBit = rand.nextInt(vis);
			while (((significantMask >> currentBit) & 1) == 1) {
				currentBit = rand.nextInt(vis);
			}
			significantMask |= (1 << currentBit);
		}
		final int sign = Constants.SIGNIFICANT_INPUTS;
		nextState = new int[states][1 << sign];
		for (int i = 0; i < states; ++i) {
			for (int j = 0; j < (1 << sign); ++j) {
				nextState[i][j] = rand.nextInt(states);
			}
		}
		moves = new Turn[states];
		Turn[] values = Turn.values();
		for (int i = 0; i < states; ++i) {
			moves[i] = values[rand.nextInt(values.length)];
		}
	}

	@Override
	public MooreMachine clone() {
		return new MooreMachine(startState, significantMask, nextState, moves);
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
		return Constants.clone(nextState);
	}

	public Turn[] getMovesArray() {
		return moves.clone();
	}

	public MooreMachine crossover(MooreMachine other) {
		final MersenneTwisterFast rand = Constants.rand;
		int startState = rand.nextBoolean() ? this.startState
				: other.startState;
		int signMask1 = this.significantMask;
		int signMask2 = other.significantMask;
		int significantMask = signMask1 & signMask2;
		final int sign = Constants.SIGNIFICANT_INPUTS;
		while (Integer.bitCount(significantMask) < sign) {
			int position = rand.nextInt(Constants.VISIBLE_CELLS);
			significantMask |= (signMask1 | signMask2) & (1 << position);
		}
		int[][] nextState = this.getNextStateArray();
		final int states = Constants.STATES_NUMBER;
		for (int i = 0; i < states; ++i) {
			int threshold = rand.nextInt((1 << sign) + 1);
			for (int j = threshold; j < (1 << sign); ++j) {
				nextState[i][j] = other.nextState[i][j];
			}
		}
		Turn[] moves = new Turn[states];
		for (int i = 0; i < states; ++i) {
			moves[i] = rand.nextBoolean() ? this.moves[i] : other.moves[i];
		}
		return new MooreMachine(startState, significantMask, nextState, moves);
	}

	private MooreMachine nextStateMutation() {
		final int states = Constants.STATES_NUMBER;
		final int masks = (1 << Constants.SIGNIFICANT_INPUTS);
		final MersenneTwisterFast rand = Constants.rand;
		int mutatedState = rand.nextInt(states);
		int mutatedMask = rand.nextInt(masks);
		int[][] nextState = this.getNextStateArray();
		int mutationResult = rand.nextInt(states);
		while (mutationResult == nextState[mutatedState][mutatedMask]) {
			mutationResult = rand.nextInt(states);
		}
		nextState[mutatedState][mutatedMask] = mutationResult;
		return new MooreMachine(this.startState, this.significantMask,
				nextState, this.moves);
	}

	private MooreMachine actionMutation() {
		final int states = Constants.STATES_NUMBER;
		final MersenneTwisterFast rand = Constants.rand;
		int mutatedState = rand.nextInt(states);
		Turn[] moves = this.getMovesArray();
		Turn[] values = Turn.values();
		int mutationResult = rand.nextInt(values.length);
		while (mutationResult == moves[mutatedState].ordinal()) {
			mutationResult = rand.nextInt(values.length);
		}
		moves[mutatedState] = values[mutationResult];
		return new MooreMachine(this.startState, this.significantMask,
				this.nextState, moves);
	}

	private MooreMachine significantInputMutation() {
		final int vis = Constants.VISIBLE_CELLS;
		final MersenneTwisterFast rand = Constants.rand;
		int wasSignificantMask = this.significantMask;
		if (Integer.bitCount(wasSignificantMask) == vis) {
			return this.clone();
		}
		int initialSignState = rand.nextInt(vis);
		while (((wasSignificantMask >> initialSignState) & 1) == 0) {
			initialSignState = rand.nextInt(vis);
		}
		int newSignState = rand.nextInt(vis);
		while (((wasSignificantMask >> newSignState) & 1) == 1) {
			newSignState = rand.nextInt(vis);
		}
		return new MooreMachine(this.startState, wasSignificantMask
				^ (1 << initialSignState) | (1 << newSignState),
				this.nextState, this.moves);
	}

	private MooreMachine startStateMutation() {
		final int states = Constants.STATES_NUMBER;
		final MersenneTwisterFast rand = Constants.rand;
		int wasStart = this.startState;
		int startState = rand.nextInt(states);
		while (wasStart == startState) {
			startState = rand.nextInt(states);
		}
		return new MooreMachine(startState, this.significantMask,
				this.nextState, this.moves);
	}

	public MooreMachine mutate() {
		MooreMachine result = this.clone();
		MersenneTwisterFast rand = Constants.rand;
		if (rand.nextDouble() <= Constants.NEXT_STATE_MUTATION_PROB) {
			result = result.nextStateMutation();
		}
		if (rand.nextDouble() <= Constants.ACTION_MUTATION_PROB) {
			result = result.actionMutation();
		}
		if (rand.nextDouble() <= Constants.SIGNIFICANT_INPUT_MUTATION_PROB) {
			result = result.significantInputMutation();
		}
		if (rand.nextDouble() <= Constants.START_STATE_MUTATION_PROB) {
			result = result.startStateMutation();
		}
		return result;
	}
	
	private String getBitString(int mask, int length) {
		char[] ans = new char[length];
		for (int i = 0; i < length; ++i) {
			ans[length - 1 - i] = (char) ('0' + ((mask >> i) & 1));
		}
		return new String(ans);
	}

	public void print(PrintWriter out) {
		out.println("Start state = " + startState);
		out.print("Significant inputs:");
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
