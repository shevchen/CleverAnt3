package core;

import ec.util.MersenneTwisterFast;

public class MooreMachineGenerator {
	public static MooreMachine generate() {
		final MersenneTwisterFast rand = Constants.rand;
		final int states = Constants.STATES_NUMBER;
		int startState = rand.nextInt(states);
		int significantMask = 0;
		final int vis = Constants.VISIBLE_CELLS;
		for (int i = 0; i < Constants.SIGNIFICANT_INPUTS; ++i) {
			int currentBit = rand.nextInt(vis);
			while (((significantMask >> currentBit) & 1) == 1) {
				currentBit = rand.nextInt(vis);
			}
			significantMask |= (1 << currentBit);
		}
		final int sign = Constants.SIGNIFICANT_INPUTS;
		int[][] nextState = new int[states][1 << sign];
		for (int i = 0; i < states; ++i) {
			for (int j = 0; j < (1 << sign); ++j) {
				nextState[i][j] = rand.nextInt(states);
			}
		}
		Turn[] moves = new Turn[states];
		Turn[] values = Turn.values();
		for (int i = 0; i < states; ++i) {
			moves[i] = values[rand.nextInt(values.length)];
		}
		try {
			return new MooreMachine(startState, significantMask, nextState,
					moves);
		} catch (InvalidAutomatonException e) {
			e.printStackTrace();
			return null;
		}
	}
}
