package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import core.Constants;
import core.MooreMachine;
import core.Turn;

public class MooreMachineParser {
	private static File findBest() throws IOException {
		File best = null;
		double bestFitness = Double.MIN_VALUE;
		String path = Constants.BEST_AUTO_DIR + "/";
		for (File f : new File(path).listFiles()) {
			File current = new File(path + f.getName() + "/"
					+ Constants.AUTO_FILENAME);
			if (!current.exists()) {
				continue;
			}
			BufferedReader br = new BufferedReader(new FileReader(current));
			StringTokenizer st = new StringTokenizer(br.readLine());
			for (int i = 0; i < 3; ++i) {
				st.nextToken();
			}
			double curFitness = Double.parseDouble(st.nextToken());
			if (curFitness > bestFitness) {
				bestFitness = curFitness;
				best = current;
			}
		}
		return best;
	}

	public static MooreMachine parseBest() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(findBest()));
		br.readLine();
		StringTokenizer st = new StringTokenizer(br.readLine());
		for (int i = 0; i < 3; ++i) {
			st.nextToken();
		}
		int startState = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < 2; ++i) {
			st.nextToken();
		}
		final int sign = Constants.SIGNIFICANT_INPUTS;
		int significantMask = 0;
		for (int i = 0; i < sign; ++i) {
			int current = Integer.parseInt(st.nextToken());
			significantMask |= 1 << current;
		}
		st = new StringTokenizer(br.readLine());
		st.nextToken();
		final int states = Constants.STATES_NUMBER;
		int[] moves = new int[states];
		for (int i = 0; i < states; ++i) {
			moves[i] = Turn.valueOf(st.nextToken()).ordinal();
		}
		br.readLine();
		int[][] nextState = new int[states][1 << sign];
		for (int i = 0; i < states; ++i) {
			for (int j = 0; j < 1 << sign; ++j) {
				st = new StringTokenizer(br.readLine());
				for (int k = 0; k < 2; ++k) {
					st.nextToken();
				}
				nextState[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		return new MooreMachine(startState, significantMask, nextState, moves);
	}
}
