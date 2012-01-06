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
		chart.setBackgroundPaint(Color.WHITE);
		return chart;
	}

	private static void readPart(BufferedReader buff, double[] data)
			throws IOException {
		for (int i = 0; i < data.length; ++i) {
			buff.readLine();
			StringTokenizer st = new StringTokenizer(buff.readLine());
			data[i] += Double.parseDouble(st.nextToken());
			buff.readLine();
		}
	}

	public static JFreeChart getGraph(Mutation m) {
		final int mut = Mutation.values().length;
		double[] prob = new double[mut];
		final int iter = Constants.ITERATIONS;
		final double[] probs = Constants.MUTATION_PROBABILITIES;
		final int graphs = probs.length;
		double[][] abscissas = new double[graphs][iter];
		double[][] ordinates = new double[graphs][iter];
		for (int pr = 0; pr < graphs; ++pr) {
			Arrays.fill(prob, Constants.COMMON_MUTATION_PROBABILITY);
			prob[m.ordinal()] = probs[pr];
			String dirName = Constants.RESULTS_DIR + "/"
					+ ResultSaver.getDirectoryName(prob) + "/";
			int succeeded = 0;
			for (File f : new File(dirName).listFiles()) {
				File full = new File(dirName + f.getName() + "/"
						+ Constants.GENERATIONS_FILENAME);
				if (!full.exists()) {
					continue;
				}
				try {
					BufferedReader buff = new BufferedReader(new FileReader(
							full));
					readPart(buff, ordinates[pr]);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				++succeeded;
			}
			for (int i = 0; i < iter; ++i) {
				abscissas[pr][i] = i + 1;
				ordinates[pr][i] /= succeeded;
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

	public static JFreeChart getBestAutoGraph() {
		final int iter = Constants.SEARCHER_ITERATIONS;
		double[][] abscissas = new double[1][iter];
		double[][] ordinates = new double[1][iter];
		String dirName = Constants.BEST_AUTO_DIR + "/";
		int succeeded = 0;
		for (File f : new File(dirName).listFiles()) {
			File full = new File(dirName + f.getName() + "/"
					+ Constants.GENERATIONS_FILENAME);
			if (!full.exists()) {
				continue;
			}
			try {
				BufferedReader buff = new BufferedReader(new FileReader(full));
				readPart(buff, ordinates[0]);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			++succeeded;
		}
		for (int i = 0; i < iter; ++i) {
			abscissas[0][i] = i + 1;
			ordinates[0][i] /= succeeded;
		}
		String graphName = "вероятности мутаций: ";
		double[] bprob = Constants.BEST_MUTATION_PROBABILITIES;
		for (int i = 0; i < bprob.length; ++i) {
			if (i > 0) {
				graphName += ", ";
			}
			graphName += Mutation.values()[i].getRuType() + " — " + bprob[i];
		}
		String xLabel = "Номер поколения";
		String yLabel = "Функция приспособленности — доля собранной еды";
		Paint[] colors = { Constants.DEFAULT_COLORS[0] };
		double[] width = { Constants.GRAPH_WIDTH };
		return createGraph(abscissas, ordinates, null,
				new String[] { graphName }, xLabel, yLabel, colors, width);
	}

	public static JFreeChart getPreliminaryGraph() {
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
			int succeeded = 0;
			for (File f : new File(dirName).listFiles()) {
				File full = new File(dirName + f.getName() + "/"
						+ Constants.GENERATIONS_FILENAME);
				if (!full.exists()) {
					continue;
				}
				try {
					BufferedReader buff = new BufferedReader(new FileReader(
							full));
					readPart(buff, ordinates[pr]);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				++succeeded;
			}
			for (int i = 0; i < iter; ++i) {
				abscissas[pr][i] = i + 1;
				ordinates[pr][i] /= succeeded;
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
