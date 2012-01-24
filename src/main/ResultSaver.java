package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

import core.Constants;
import core.Mutation;
import core.SimulationResult;

public class ResultSaver {
	private PrintWriter outGen;
	private final String curDir;

	public static String getDirectoryName(double[] prob) {
		String name = "";
		for (Mutation m : Mutation.values()) {
			if (m.ordinal() > 0) {
				name += ";";
			}
			name += String.format(Locale.US, "%.2f", prob[m.ordinal()]);
		}
		return name;
	}

	public ResultSaver(final String dirName) {
		long id = (System.currentTimeMillis() * Thread.currentThread()
				.hashCode())
				& Integer.MAX_VALUE;
		curDir = dirName + "/" + id + "/";
		try {
			new File(curDir).mkdirs();
			outGen = new PrintWriter(new File(curDir
					+ Constants.GENERATIONS_FILENAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		outGen.close();
	}

	public void clear() {
		try {
			outGen.close();
			outGen = new PrintWriter(new File(curDir
					+ Constants.GENERATIONS_FILENAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void saveAutomaton(SimulationResult sr) {
		try {
			PrintWriter outAuto = new PrintWriter(new File(curDir
					+ Constants.AUTO_FILENAME));
			outAuto.printf(Locale.US, "Eaten part = %.6f", sr
					.getMeanEatenPart());
			outAuto.println();
			sr.getAuto().print(outAuto);
			outAuto.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void saveGeneration(int genNumber, double part) {
		outGen.println("Generation " + genNumber);
		outGen.printf(Locale.US, "%.6f = eaten part", part);
		outGen.println();
		outGen.println();
	}
}
