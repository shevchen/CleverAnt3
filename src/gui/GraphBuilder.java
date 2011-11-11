package gui;

import java.awt.BasicStroke;
import java.awt.Paint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;

public class GraphBuilder {
	public static ChartPanel createGraph(double[][] abscissas,
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
		return new ChartPanel(chart);
	}
}
