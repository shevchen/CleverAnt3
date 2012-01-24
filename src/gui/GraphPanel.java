package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

import main.ResultSaver;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import core.Constants;
import core.Mutation;

public class GraphPanel {
	private static JFreeChart createGraph(double[][] abscissas,
			double[][] ordinates, String title, String[] graphNames,
			String xLabel, String yLabel, Paint[] colors, double[] width) {
		int graphs = abscissas.length;
		DefaultXYDataset data = new DefaultXYDataset();
		for (int i = 0; i < graphs; ++i) {
			data.addSeries(graphNames[i], new double[][] { abscissas[i],
					ordinates[i] });
		}
		JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel,
				yLabel, data, PlotOrientation.VERTICAL, true, false, false);
		XYItemRenderer ren = chart.getXYPlot().getRenderer();
		for (int i = 0; i < graphs; ++i) {
			ren.setSeriesStroke(i, new BasicStroke((float) width[i],
					BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			ren.setSeriesPaint(i, colors[i]);
		}
		chart.getPlot().setBackgroundPaint(Color.WHITE);
		return chart;
	}

	private static void updatePart(BufferedReader buff, double[] data,
			boolean max) throws IOException {
		for (int i = 0; i < data.length; ++i) {
			buff.readLine();
			StringTokenizer st = new StringTokenizer(buff.readLine());
			double fitness = Double.parseDouble(st.nextToken());
			if (max) {
				data[i] = Math.max(data[i], fitness);
			} else {
				data[i] += fitness;
			}
			buff.readLine();
		}
	}

	private static void getResult(String dirName, double[] data, boolean max) {
		int succeeded = 0;
		System.err.println(dirName);
		for (File f : new File(dirName).listFiles()) {
			File gen = new File(dirName + f.getName() + "/"
					+ Constants.GENERATIONS_FILENAME);
			if (!gen.exists()) {
				continue;
			}
			try {
				updatePart(new BufferedReader(new FileReader(gen)), data, max);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		if (max || succeeded == 0) {
			return;
		}
		for (int i = 0; i < data.length; ++i) {
			data[i] /= succeeded;
		}
	}

	public static JFreeChart getGraph(Mutation m, boolean max) {
		final int mut = Mutation.values().length;
		double[] prob = new double[mut];
		final int iter = Constants.RUNNER_ITERATIONS;
		final double[] probs = Constants.MUTATION_PROBABILITIES;
		final int graphs = probs.length;
		double[][] abscissas = new double[graphs][iter];
		double[][] ordinates = new double[graphs][iter];
		for (int pr = 0; pr < graphs; ++pr) {
			Arrays.fill(prob, Constants.COMMON_MUTATION_PROBABILITY);
			prob[m.ordinal()] = probs[pr];
			String dirName = Constants.RESULTS_DIR + "/" + m + "/"
					+ ResultSaver.getDirectoryName(prob) + "/";
			getResult(dirName, ordinates[pr], max);
			for (int i = 0; i < iter; ++i) {
				abscissas[pr][i] = i + 1;
			}
		}
		String[] graphNames = new String[probs.length];
		for (int i = 0; i < graphs; ++i) {
			graphNames[i] = "вероятность мутации " + probs[i];
		}
		String xLabel = "Номер поколения";
		String yLabel = "Функция приспособленности — доля собранной еды";
		Paint[] colors = Arrays.copyOf(Constants.DEFAULT_COLORS, graphs);
		double[] width = new double[graphs];
		Arrays.fill(width, Constants.GRAPH_WIDTH);
		return createGraph(abscissas, ordinates, null, graphNames, xLabel,
				yLabel, colors, width);
	}

	public static JFreeChart getBestAutoGraph(boolean max) {
		final int iter = Constants.SEARCHER_ITERATIONS;
		double[][] abscissas = new double[2][iter];
		double[][] ordinates = new double[2][iter];
		double[] prelimBest = new double[Mutation.values().length];
		Arrays.fill(prelimBest, Constants.COMMON_MUTATION_PROBABILITY);
		for (int i = 0; i < 2; ++i) {
			String dirName = i == 0 ? (Constants.BEST_AUTO_DIR + "/")
					: (Constants.PRELIMINARY_RESULTS_DIR + "/"
							+ ResultSaver.getDirectoryName(prelimBest) + "/");
			getResult(dirName, ordinates[i], max);
			for (int j = 0; j < iter; ++j) {
				abscissas[i][j] = j + 1;
			}
		}
		String[] graphNames = {
				"Вероятности мутаций:\n",
				"Предварительные результаты: вероятности мутаций — по "
						+ Constants.COMMON_MUTATION_PROBABILITY };
		double[] bprob = Constants.BEST_MUTATION_PROBABILITIES;
		for (int i = 0; i < bprob.length; ++i) {
			if (i > 0) {
				graphNames[0] += ", ";
			}
			graphNames[0] += Mutation.values()[i].getRuType() + " — "
					+ bprob[i];
		}
		String xLabel = "Номер поколения";
		String yLabel = "Функция приспособленности — доля собранной еды";
		Paint[] colors = Arrays.copyOf(Constants.DEFAULT_COLORS, 2);
		double[] width = new double[2];
		Arrays.fill(width, Constants.GRAPH_WIDTH);
		return createGraph(abscissas, ordinates, null, graphNames, xLabel,
				yLabel, colors, width);
	}

	public static JFreeChart getPreliminaryGraph(boolean max) {
		final int mut = Mutation.values().length;
		double[] prob = new double[mut];
		final int iter = Constants.SEARCHER_ITERATIONS;
		final double[] probs = Constants.MUTATION_PROBABILITIES;
		final int graphs = probs.length;
		double[][] abscissas = new double[graphs][iter];
		double[][] ordinates = new double[graphs][iter];
		for (int pr = 0; pr < graphs; ++pr) {
			Arrays.fill(prob, probs[pr]);
			String dirName = Constants.PRELIMINARY_RESULTS_DIR + "/"
					+ ResultSaver.getDirectoryName(prob) + "/";
			getResult(dirName, ordinates[pr], max);
			for (int i = 0; i < iter; ++i) {
				abscissas[pr][i] = i + 1;
			}
		}
		String[] graphNames = new String[probs.length];
		for (int i = 0; i < graphs; ++i) {
			graphNames[i] = "вероятности мутаций " + probs[i];
		}
		String xLabel = "Номер поколения";
		String yLabel = "Функция приспособленности — доля собранной еды";
		Paint[] colors = Arrays.copyOf(Constants.DEFAULT_COLORS, graphs);
		double[] width = new double[graphs];
		Arrays.fill(width, Constants.GRAPH_WIDTH);
		return createGraph(abscissas, ordinates, null, graphNames, xLabel,
				yLabel, colors, width);
	}
}
