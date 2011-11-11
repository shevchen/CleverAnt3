package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

import core.SimulationResult;

public class ResultSaver {
	public static final String RESULTS_DIR = "results";

	private long startTime;
	private PrintWriter outGen;
	private String curDir;

	public ResultSaver(double[] prob) {
		Locale.setDefault(Locale.US);
		startTime = System.currentTimeMillis();
		String ident = "";
		for (int i = 0; i < prob.length; ++i) {
			if (i > 0) {
				ident += "|";
			}
			ident += String.format("%4.2f", prob[i]);
		}
		curDir = RESULTS_DIR + "/" + ident + "/" + startTime + "/";
		try {
			new File(curDir).mkdirs();
			outGen = new PrintWriter(new File(curDir + "generations"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		outGen.close();
	}

	public void saveAutomaton(SimulationResult sr) {
		try {
			PrintWriter outAuto = new PrintWriter(new File(curDir + "auto"));
			outAuto.println("Eaten part = "
					+ (sr.eatenPartsSum / sr.fieldsTested));
			sr.auto.print(outAuto);
			outAuto.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void saveGeneration(int genNumber, double best, double mean) {
		outGen.println("Generation " + genNumber);
		outGen.println("Max eaten part = " + best);
		outGen.println("Mean eaten part = " + mean);
		outGen.println();
	}
}
