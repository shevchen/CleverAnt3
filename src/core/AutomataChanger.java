package core;

import java.util.Random;

public class AutomataChanger {
	private static MooreMachine nextStateMutation(MooreMachine m) {
		final int states = Constants.STATES_NUMBER;
		final int masks = (1 << Constants.VISIBLE_CELLS);
		final Random rand = Constants.rand;
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
		final Random rand = Constants.rand;
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
		final Random rand = Constants.rand;
		int wasSignificantMask = m.getSignificantMask();
		if (Integer.bitCount(wasSignificantMask) == vis) {
			return m.clone();
		}
		int initialSignState = rand.nextInt(vis);
		while (((wasSignificantMask >> initialSignState) & 1) == 0) {
			initialSignState = rand.nextInt(vis);
		}
		int newSignState = rand.nextInt(vis);
		while (((wasSignificantMask >> initialSignState) & 1) == 1) {
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
		final Random rand = Constants.rand;
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
		double sum = 0.;
		double currentProb = Constants.rand.nextDouble();
		sum += Constants.NEXT_STATE_MUTATION_PROB;
		if (currentProb <= sum) {
			return nextStateMutation(m);
		}
		sum += Constants.ACTION_MUTATION_PROB;
		if (currentProb <= sum) {
			return actionMutation(m);
		}
		sum += Constants.SIGNIFICANT_INPUT_MUTATION_PROB;
		if (currentProb <= sum) {
			return significantInputMutation(m);
		}
		sum += Constants.START_STATE_MUTATION_PROB;
		if (currentProb <= sum) {
			return startStateMutation(m);
		}
		return m.clone();
	}

	public static MooreMachine crossover(MooreMachine m1, MooreMachine m2) {
		return null;
	}
}
