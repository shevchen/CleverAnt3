package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import core.Field;
import core.MooreMachine;
import core.MooreMachineGenerator;
import core.SimulationResult;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File("test.txt"));
		int best = -1;
		Field f = new Field(89);
		f.print(out);
		out.println();
		MooreMachine mbest = null;
		for (int i = 0; i < 10000; ++i) {
			SimulationResult sim = f.simulate(MooreMachineGenerator.generate());
			if (sim.eaten > best) {
				mbest = sim.auto;
				best = sim.eaten;
			}
		}
		out.println(best);
		out.println();
		mbest.print(out);
		out.close();
	}
}
