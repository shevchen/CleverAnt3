package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import core.AutomataChanger;
import core.MooreMachine;
import core.MooreMachineGenerator;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File("test.txt"));
		MooreMachine m1 = MooreMachineGenerator.generate();
		MooreMachine m2 = MooreMachineGenerator.generate();
		MooreMachine m3 = AutomataChanger.crossover(m1, m2);
		m1.print(out);
		out.println();
		m2.print(out);
		out.println();
		m3.print(out);
		out.close();
	}
}
