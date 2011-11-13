package main;

import gui.GraphBuilder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Paint;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;

import core.Constants;
import core.Mutation;
import core.Processor;

public class Runner {
	public static void main(String[] args) throws InterruptedException {
		final int thr = Constants.THREADS;
		Thread[] proc = new Thread[thr];
		for (int i = 0; i < thr; ++i) {
			proc[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < Constants.RUNNINGS_PER_THREAD; ++i) {
						for (Mutation m : Mutation.values()) {
							for (double prob : Constants.MUTATION_PROBABILITIES) {
								Processor.run(m, prob);
							}
						}
					}
				}
			});
		}
		for (int i = 0; i < thr; ++i) {
			proc[i].start();
		}
		Thread guiThread = new Thread(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.getContentPane()
						.setLayout(
								new BoxLayout(frame.getContentPane(),
										BoxLayout.Y_AXIS));
				double[][] x = new double[][] { { 1, 4, 5 } };
				double[][] y = new double[][] { { 3, 1.5, 8 } };
				String[] graphNames = new String[] { "graphName" };
				Paint[] colors = new Paint[] { Color.RED };
				double[] widths = new double[] { 2 };
				ChartPanel panel = GraphBuilder.createGraph(x, y, "title",
						graphNames, "xLabel", "yLabel", colors, widths);
				frame.add(panel);
				frame.setMinimumSize(new Dimension(300, 300));
				frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
		// guiThread.start();
	}
}
