package core;

import ec.util.MersenneTwisterFast;

public class AutomataChanger {
	private static MooreMachine nextStateMutation(MooreMachine m) {
		final int states = Constants.STATES_NUMBER;
		final int masks = (1 << Constants.SIGNIFICANT_INPUTS);
		final MersenneTwisterFast rand = Constants.rand;
		int mutatedState = rand.nextInt(states);
		int mutatedMask = rand.nextInt(masks);
		int[][] nextState = m.getNextStateArray();
		int mutationResult = rand.nextInt(states);
		while (mutationResult == nextState[mutatedState][mutatedMask]) {
			mutationResult = rand.nextInt(states);
		}
		nextState[mutatedState][mutatedMask] = mutationResult;
		try {
			return new MooreMachine(m.getStartState(), m.getSignificantMask(),
					nextState, m.getMovesArray());
		} catch (InvalidAutomatonException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static MooreMachine actionMutation(MooreMachine m) {
		final int states = Constants.STATES_NUMBER;
		final MersenneTwisterFast rand = Constants.rand;
		int mutatedState = rand.nextInt(states);
		Turn[] moves = m.getMovesArray();
		Turn[] values = Turn.values();
		int mutationResult = rand.nextInt(values.length);
		while (mutationResult == moves[mutatedState].ordinal()) {
			mutationResult = rand.nextInt(values.length);
		}
		moves[mutatedState] = values[mutationResult];
		try {
			return new MooreMachine(m.getStartState(), m.getSignificantMask(),
					m.getNextStateArray(), moves);
		} catch (InvalidAutomatonException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static MooreMachine significantInputMutation(MooreMachine m) {
		final int vis = Constants.VISIBLE_CELLS;
		final MersenneTwisterFast rand = Constants.rand;
		int wasSignificantMask = m.getSignificantMask();
		if (Integer.bitCount(wasSignificantMask) == vis) {
			return m.clone();
		}
		int initialSignState = rand.nextInt(vis);
		while (((wasSignificantMask >> initialSignState) & 1) == 0) {
			initialSignState = rand.nextInt(vis);
		}
		int newSignState = rand.nextInt(vis);
		while (((wasSignificantMask >> newSignState) & 1) == 1) {
			newSignState = rand.nextInt(vis);
		}
		try {
			return new MooreMachine(m.getStartState(), wasSignificantMask
					^ (1 << initialSignState) | (1 << newSignState), m
					.getNextStateArray(), m.getMovesArray());
		} catch (InvalidAutomatonException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static MooreMachine startStateMutation(MooreMachine m) {
		final int states = Constants.STATES_NUMBER;
		final MersenneTwisterFast rand = Constants.rand;
		int wasStart = m.getStartState();
		int startState = rand.nextInt(states);
		while (wasStart == startState) {
			startState = rand.nextInt(states);
		}
		try {
			return new MooreMachine(startState, m.getSignificantMask(), m
					.getNextStateArray(), m.getMovesArray());
		} catch (InvalidAutomatonException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static MooreMachine mutate(MooreMachine m) {
		MooreMachine result = m.clone();
		MersenneTwisterFast rand = Constants.rand;
		if (rand.nextDouble() <= Constants.NEXT_STATE_MUTATION_PROB) {
			result = nextStateMutation(result);
		}
		if (rand.nextDouble() <= Constants.ACTION_MUTATION_PROB) {
			result = actionMutation(result);
		}
		if (rand.nextDouble() <= Constants.SIGNIFICANT_INPUT_MUTATION_PROB) {
			result = significantInputMutation(result);
		}
		if (rand.nextDouble() <= Constants.START_STATE_MUTATION_PROB) {
			result = startStateMutation(result);
		}
		return result;
	}

	public static MooreMachine crossover(MooreMachine m1, MooreMachine m2) {
		final MersenneTwisterFast rand = Constants.rand;
		int startState = rand.nextBoolean() ? m1.getStartState() : m2
				.getStartState();
		int signMask1 = m1.getSignificantMask();
		int signMask2 = m2.getSignificantMask();
		int significantMask = signMask1 & signMask2;
		final int sign = Constants.SIGNIFICANT_INPUTS;
		while (Integer.bitCount(significantMask) < sign) {
			int position = rand.nextInt(Constants.VISIBLE_CELLS);
			significantMask |= (signMask1 | signMask2) & (1 << position);
		}
		int[][] nextState = m1.getNextStateArray();
		final int states = Constants.STATES_NUMBER;
		for (int i = 0; i < states; ++i) {
			int threshold = rand.nextInt((1 << sign) + 1);
			for (int j = threshold; j < (1 << sign); ++j) {
				nextState[i][j] = m2.getNextState(i, j);
			}
		}
		Turn[] moves = new Turn[states];
		for (int i = 0; i < states; ++i) {
			moves[i] = rand.nextBoolean() ? m1.getMove(i) : m2.getMove(i);
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
