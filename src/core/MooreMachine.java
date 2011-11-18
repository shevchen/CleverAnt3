package core;

import java.io.PrintWriter;

import ec.util.MersenneTwister;

public class MooreMachine {
	private int startState;
	private int significantMask;
	private int[][] nextState;
	private Turn[] moves;

	public MooreMachine(int startState, int significantMask, int[][] nextState,
			Turn[] moves) {
		this.startState = startState;
		this.significantMask = significantMask;
		this.nextState = nextState;
		this.moves = moves;
	}

	public MooreMachine() {
		final MersenneTwister rand = Constants.rand;
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

	public MooreMachine crossover(MooreMachine other) {
		final MersenneTwister rand = Constants.rand;
		int startState = rand.nextBoolean() ? this.startState
				: other.startState;
		int signMask1 = this.significantMask;
		int signMask2 = other.significantMask;
		int significantMask = signMask1 & signMask2;
		final int sign = Constants.SIGNIFICANT_INPUTS;
		final int vis = Constants.VISIBLE_CELLS;
		while (Integer.bitCount(significantMask) < sign) {
			int position = rand.nextInt(vis);
			significantMask |= (signMask1 | signMask2) & (1 << position);
		}
		final int states = Constants.STATES_NUMBER;
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
				int weight1 = 1 << Integer.bitCount(detMask1);
				int weight2 = 1 << Integer.bitCount(detMask2);
				int[] votes = new int[states];
				for (int mask = 0; mask < (1 << sign); ++mask) {
					if ((mask & detMask1) == mask1) {
						votes[this.nextState[i][mask]] += weight1;
					}
					if ((mask & detMask2) == mask2) {
						votes[other.nextState[i][mask]] += weight2;
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

	private void nextStateMutation() {
		final int states = Constants.STATES_NUMBER;
		final int masks = (1 << Constants.SIGNIFICANT_INPUTS);
		final MersenneTwister rand = Constants.rand;
		int mutatedState = rand.nextInt(states);
		int mutatedMask = rand.nextInt(masks);
		int mutationResult = rand.nextInt(states);
		while (mutationResult == nextState[mutatedState][mutatedMask]) {
			mutationResult = rand.nextInt(states);
		}
		nextState[mutatedState][mutatedMask] = mutationResult;
	}

	private void actionMutation() {
		final int states = Constants.STATES_NUMBER;
		final MersenneTwister rand = Constants.rand;
		int mutatedState = rand.nextInt(states);
		Turn[] values = Turn.values();
		int mutationResult = rand.nextInt(values.length);
		while (mutationResult == moves[mutatedState].ordinal()) {
			mutationResult = rand.nextInt(values.length);
		}
		moves[mutatedState] = values[mutationResult];
	}

	private void significantPredicateMutation() {
		final int vis = Constants.VISIBLE_CELLS;
		assert (vis == 8);
		final MersenneTwister rand = Constants.rand;
		int initialSignState = rand.nextInt(vis);
		while (((significantMask >> initialSignState) & 1) == 0) {
			initialSignState = rand.nextInt(vis);
		}
		int newSignState = rand.nextInt(vis);
		while (((significantMask >> newSignState) & 1) == 1) {
			newSignState = rand.nextInt(vis);
		}
		significantMask = significantMask ^ (1 << initialSignState)
				| (1 << newSignState);
	}

	private void startStateMutation() {
		final int states = Constants.STATES_NUMBER;
		final MersenneTwister rand = Constants.rand;
		int newStartState = rand.nextInt(states);
		while (newStartState == startState) {
			newStartState = rand.nextInt(states);
		}
		startState = newStartState;
	}

	public void mutate(Mutation m) {
		switch (m) {
		case NEXT_STATE_MUTATION:
			nextStateMutation();
			return;
		case ACTION_MUTATION:
			actionMutation();
			return;
		case SIGNIFICANT_PREDICATE_MUTATION:
			significantPredicateMutation();
			return;
		case START_STATE_MUTATION:
			startStateMutation();
			return;
		}
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
