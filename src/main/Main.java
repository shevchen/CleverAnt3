package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import core.MooreMachine;
import core.MooreMachineGenerator;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File("test.txt"));
		for (int i = 0; i < 10; ++i) {
			MooreMachine m = MooreMachineGenerator.generate();
			m.print(out);
			out.println();
		}
		out.close();
	}
}
