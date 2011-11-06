package gui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;

import java.util.List;

public class GraphicPanel extends JPanel {

	private final ArrayList<ArrayList<Double>> values;
	private final List<String> names;

	public GraphicPanel(ArrayList<ArrayList<Double>> values, List<String> names) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.values = values;
		this.names = names;
		ChartPanel chartPanel = new ChartPanel(
				createChart(createDataset(values)), false);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		this.add(chartPanel);
	}

	public void updateGraphic() {
		this.removeAll();
		ChartPanel chartPanel = new ChartPanel(
				createChart(createDataset(values)), false);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		this.add(chartPanel);
		revalidate();
	}

	private XYDataset createDataset(ArrayList<ArrayList<Double>> values) {
		DefaultXYDataset dataset = new DefaultXYDataset();
		if (!values.isEmpty()) {
			final int n = values.get(0).size();
			double[] x = new double[n];
			for (int i = 0; i < n; i++) {
				x[i] = i;
			}
			int j = 0;
			for (ArrayList<Double> val : values) {
				synchronized (val) {
					double[] y = new double[n];
					for (int i = 0; i < n; i++) {
						y[i] = val.get(i);
					}
					double[][] data = new double[][] { x, y };
					dataset.addSeries(names.get(j), data);
				}
				j++;
			}
		}
		return dataset;
	}

	private static JFreeChart createChart(XYDataset dataset) {

		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart("", // chart title
				"Generation's index", // domain axis label
				"Standard fitness-function", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);

		XYItemRenderer renderer = plot.getRenderer();

		BasicStroke stroke = new BasicStroke(3);

		for (int i = 0; i < 5; i++) {
			renderer.setSeriesStroke(i, stroke);
		}

		// set up gradient paints for series...
		GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f,
				0.0f, new Color(0, 0, 64));
		GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f,
				0.0f, new Color(0, 64, 0));
		GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f,
				0.0f, new Color(64, 0, 0));
		GradientPaint gp3 = new GradientPaint(0.0f, 0.0f, Color.cyan, 0.0f,
				0.0f, Color.cyan);
		GradientPaint gp4 = new GradientPaint(0.0f, 0.0f, Color.pink, 0.0f,
				0.0f, Color.pink);
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
		renderer.setSeriesPaint(2, gp2);
		renderer.setSeriesPaint(3, gp3);
		renderer.setSeriesPaint(4, gp4);
		/*
		 * ValueAxis domainAxis = plot.getDomainAxis();
		 * domainAxis.setsetCategoryLabelPositions(
		 * CategoryLabelPositions.createUpRotationLabelPositions( Math.PI /
		 * 6.0));
		 */
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}
}