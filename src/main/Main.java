package main;

import gui.GraphBuilder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Paint;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;

import core.Processor;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Thread mainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Processor.process(true);
			}
		});
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
		mainThread.start();
		guiThread.start();
	}
}
