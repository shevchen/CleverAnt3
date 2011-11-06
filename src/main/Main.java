package main;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.DefaultXYDataset;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		Thread mainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Processor.process();
			}
		});
		Thread guiThread = new Thread(new Runnable() {
			@Override
			public void run() {
				DefaultXYDataset data = new DefaultXYDataset();
				data.addSeries("abc",
						new double[][] { { 0, 1, 2 }, { 6, 4, 9 } });
				data.addSeries("def",
						new double[][] { { 0, 1, 2 }, { 6, 4, 9 } });
				JFreeChart chart = ChartFactory
						.createXYLineChart("title", "x", "y", data,
								PlotOrientation.VERTICAL, true, false, false);

				ChartPanel panel = new ChartPanel(chart);

				DefaultXYDataset data2 = new DefaultXYDataset();
				data2.addSeries("abc", new double[][] { { 0, 1, 2 },
						{ 6, 4, 1 } });
				JFreeChart chart2 = ChartFactory.createXYLineChart("title2",
						"x2", "y2", data2, PlotOrientation.VERTICAL, true,
						false, false);
				XYPlot plot = chart2.getXYPlot();
				XYDotRenderer ren = new XYDotRenderer();
				ren.setDotHeight(3);
				ren.setDotWidth(3);
				plot.setRenderer(ren);
				ChartPanel panel2 = new ChartPanel(chart2);

				JFrame frame = new JFrame();
				frame.getContentPane()
						.setLayout(
								new BoxLayout(frame.getContentPane(),
										BoxLayout.Y_AXIS));
				frame.add(panel);
				frame.add(panel2);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
		mainThread.start();
		guiThread.start();
	}
}
