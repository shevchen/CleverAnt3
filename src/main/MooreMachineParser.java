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
	public static MooreMachine parse(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
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
		Turn[] moves = new Turn[states];
		for (int i = 0; i < states; ++i) {
			moves[i] = Turn.valueOf(st.nextToken());
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
