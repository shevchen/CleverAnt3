package core;

import java.io.PrintWriter;

import ec.util.MersenneTwister;

public class MooreMachine implements Cloneable {
	private int startState;
	private int significantMask;
	private int[][] nextState;
	private Turn[] moves;

	private MooreMachine(int startState, int significantMask,
			int[][] nextState, Turn[] moves) {
		this.startState = startState;
		this.significantMask = significantMask;
		this.nextState = Utils.clone(nextState);
		this.moves = moves.clone();
	}

	public MooreMachine() {
		final MersenneTwister rand = Values.rand;
		final int states = Values.STATES_NUMBER;
		startState = rand.nextInt(states);
		significantMask = 0;
		final int vis = Values.VISIBLE_CELLS;
		for (int i = 0; i < Values.SIGNIFICANT_INPUTS; ++i) {
			int currentBit = rand.nextInt(vis);
			while (((significantMask >> currentBit) & 1) == 1) {
				currentBit = rand.nextInt(vis);
			}
			significantMask |= (1 << currentBit);
		}
		final int sign = Values.SIGNIFICANT_INPUTS;
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
		return Utils.clone(nextState);
	}

	public Turn[] getMovesArray() {
		return moves.clone();
	}

	public MooreMachine crossover(MooreMachine other) {
		final MersenneTwister rand = Values.rand;
		int startState = rand.nextBoolean() ? this.startState
				: other.startState;
		int signMask1 = this.significantMask;
		int signMask2 = other.significantMask;
		int significantMask = signMask1 & signMask2;
		final int sign = Values.SIGNIFICANT_INPUTS;
		final int vis = Values.VISIBLE_CELLS;
		while (Integer.bitCount(significantMask) < sign) {
			int position = rand.nextInt(vis);
			significantMask |= (signMask1 | signMask2) & (1 << position);
		}
		final int states = Values.STATES_NUMBER;
		int[][] newNextState = new int[states][1 << sign];
		for (int i = 0; i < states; ++i) {
			for (int j = 0; j < (1 << sign); ++j) {
				int pos = 0;
				int pos1 = 0, pos2 = 0;
				int mask1 = 0, mask2 = 0;
				int detMask1 = 0, detMask2 = 0;
				for (int k = 0; k < vis; ++k) {
					if (((significantMask >> k) & 1) == 1) {
						if (((signMask1 >> k) & 1) == 1) {
							detMask1 |= 1 << pos1;
							mask1 |= ((j >> pos) & 1) << pos1;
							++pos1;
						}
						if (((signMask2 >> k) & 1) == 1) {
							detMask2 |= 1 << pos2;
							mask2 |= ((j >> pos) & 1) << pos2;
							++pos2;
						}
						++pos;
					} else {
						if (((signMask1 >> k) & 1) == 1) {
							++pos1;
						}
						if (((signMask2 >> k) & 1) == 1) {
							++pos2;
						}
					}
				}
				int[] votes = new int[states];
				for (int mask = 0; mask < (1 << sign); ++mask) {
					if ((mask & detMask1) == mask1) {
						votes[this.nextState[i][mask]]++;
					}
					if ((mask & detMask2) == mask2) {
						votes[other.nextState[i][mask]]++;
					}
				}
				int maxScore = -1, leaders = 0;
				for (int k = 0; k < states; ++k) {
					if (votes[k] > maxScore) {
						maxScore = votes[k];
						leaders = 1;
					} else if (votes[k] == maxScore) {
						++leaders;
					}
				}
				int result = rand.nextInt(leaders);
				for (int k = states - 1; k >= 0; --k) {
					if (votes[k] == maxScore && (--leaders) == result) {
						newNextState[i][j] = k;
						break;
					}
				}
			}
		}
		Turn[] moves = new Turn[states];
		for (int i = 0; i < states; ++i) {
			moves[i] = rand.nextBoolean() ? this.moves[i] : other.moves[i];
		}
		return new MooreMachine(startState, significantMask, newNextState,
				moves);
	}

	public MooreMachine nextStateMutation() {
		final int states = Values.STATES_NUMBER;
		final int masks = (1 << Values.SIGNIFICANT_INPUTS);
		final MersenneTwister rand = Values.rand;
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

	public MooreMachine actionMutation() {
		final int states = Values.STATES_NUMBER;
		final MersenneTwister rand = Values.rand;
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

	public MooreMachine significantInputMutation() {
		final int vis = Values.VISIBLE_CELLS;
		final MersenneTwister rand = Values.rand;
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

	public MooreMachine startStateMutation() {
		final int states = Values.STATES_NUMBER;
		final MersenneTwister rand = Values.rand;
		int wasStart = this.startState;
		int startState = rand.nextInt(states);
		while (wasStart == startState) {
			startState = rand.nextInt(states);
		}
		return new MooreMachine(startState, this.significantMask,
				this.nextState, this.moves);
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
		final int vis = Values.VISIBLE_CELLS;
		for (int i = 0; i < vis; ++i) {
			if (((significantMask >> i) & 1) == 1) {
				out.print(" " + i);
			}
		}
		out.println();
		final int states = Values.STATES_NUMBER;
		out.print("Moves:");
		for (int i = 0; i < states; ++i) {
			out.print(" " + moves[i]);
		}
		out.println();
		out.printf("%15s%15s%15s", "state", "inputs", "next state");
		out.println();
		final int sign = Values.SIGNIFICANT_INPUTS;
		for (int i = 0; i < states; ++i) {
			for (int j = 0; j < (1 << sign); ++j) {
				out.printf("%15d%15s%15d", i, getBitString(j,
						Values.SIGNIFICANT_INPUTS), nextState[i][j]);
				out.println();
			}
		}
	}
}
